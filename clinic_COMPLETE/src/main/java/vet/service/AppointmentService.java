package main.java.vet.service;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import main.java.vet.model.*;
import main.java.vet.util.DatabaseConnection;
import main.java.vet.exception.NotificationException;

public class AppointmentService extends BaseService {
    private final AppointmentValidator validator;
    private final NotificationService notificationService;
    private final AuditService auditService;
    private final ResourceService resourceService;
    private final ClientService clientService;
    private final Logger logger = Logger.getLogger(AppointmentService.class.getName());

    public AppointmentService(ServiceFactory serviceFactory) {
        super(serviceFactory);
        this.validator = new AppointmentValidator(serviceFactory.getResourceService());
        this.notificationService = serviceFactory.getNotificationService();
        this.auditService = serviceFactory.getAuditService();
        this.resourceService = serviceFactory.getResourceService();
        this.clientService = serviceFactory.getClientService();
    }

    public void scheduleAppointment(Appointment appointment) throws SQLException {
        try {
            // Validate appointment
            validator.validateAppointment(appointment);
            
            // Set appointment end time based on service duration
            validator.setAppointmentEndTime(appointment);
            
            // Check resource availability
            if (!resourceService.checkResourceAvailability(appointment)) {
                throw new SQLException("Required resources are not available for this time slot");
            }
            
            // Begin transaction
            Connection conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false);
            
            try {
                // Insert appointment into database
                String query = "INSERT INTO appointments (client_id, pet_id, service_type, start_time, end_time, status, price, notes) " +
                             "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
                
                try (PreparedStatement stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
                    stmt.setInt(1, appointment.getClientId());
                    stmt.setInt(2, appointment.getPetId());
                    stmt.setString(3, appointment.getServiceType().name());
                    stmt.setTimestamp(4, appointment.getStartTime());
                    stmt.setTimestamp(5, appointment.getEndTime());
                    stmt.setString(6, "SCHEDULED");
                    stmt.setDouble(7, appointment.getPrice());
                    stmt.setString(8, appointment.getNotes());
                    
                    stmt.executeUpdate();
                    
                    try (ResultSet rs = stmt.getGeneratedKeys()) {
                        if (rs.next()) {
                            appointment.setAppointmentId(rs.getInt(1));
                        }
                    }
                }
                
                // Reserve resources
                resourceService.reserveResources(appointment);
                
                // Send confirmation email
                Client client = clientService.getClientById(appointment.getClientId());
                notificationService.sendAppointmentConfirmation(
                    appointment.getAppointmentId(),
                    client.getEmail(),
                    appointment.getStartTime()
                );
                
                // Log the action
                auditService.logAction("Appointment scheduled", "System");
                
                // Commit transaction
                conn.commit();
                
                logger.info("Appointment scheduled successfully with ID: " + appointment.getAppointmentId());
            } catch (Exception e) {
                // Rollback transaction on error
                conn.rollback();
                throw e;
            } finally {
                conn.setAutoCommit(true);
                conn.close();
            }
        } catch (Exception e) {
            logger.severe("Failed to schedule appointment: " + e.getMessage());
            throw new SQLException("Failed to schedule appointment: " + e.getMessage(), e);
        }
    }

    public void updateAppointment(Appointment appointment) throws SQLException {
        try {
            validator.validateAppointment(appointment);
            validator.setAppointmentEndTime(appointment);
            
            if (!resourceService.checkResourceAvailability(appointment)) {
                throw new SQLException("Required resources are not available for this time slot");
            }
            
            Connection conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false);
            
            try {
                String query = "UPDATE appointments SET service_type = ?, start_time = ?, end_time = ?, " +
                             "status = ?, price = ?, notes = ? WHERE appointment_id = ?";
                
                try (PreparedStatement stmt = conn.prepareStatement(query)) {
                    stmt.setString(1, appointment.getServiceType().name());
                    stmt.setTimestamp(2, appointment.getStartTime());
                    stmt.setTimestamp(3, appointment.getEndTime());
                    stmt.setString(4, appointment.getStatus());
                    stmt.setDouble(5, appointment.getPrice());
                    stmt.setString(6, appointment.getNotes());
                    stmt.setInt(7, appointment.getAppointmentId());
                    
                    stmt.executeUpdate();
                }
                
                // Update resource reservations
                resourceService.updateResourceReservation(appointment);
                
                // Send update notification
                Client client = clientService.getClientById(appointment.getClientId());
                notificationService.sendAppointmentConfirmation(
                    appointment.getAppointmentId(),
                    client.getEmail(),
                    appointment.getStartTime()
                );
                
                // Log the action
                auditService.logAction("Appointment updated", "System");
                
                conn.commit();
                
                logger.info("Appointment updated successfully with ID: " + appointment.getAppointmentId());
            } catch (Exception e) {
                conn.rollback();
                throw e;
            } finally {
                conn.setAutoCommit(true);
                conn.close();
            }
        } catch (Exception e) {
            logger.severe("Failed to update appointment: " + e.getMessage());
            throw new SQLException("Failed to update appointment: " + e.getMessage(), e);
        }
    }

    public void cancelAppointment(int appointmentId) throws SQLException {
        try {
            Appointment appointment = getAppointmentById(appointmentId);
            if (appointment == null) {
                throw new SQLException("Appointment not found");
            }

            if ("CANCELLED".equals(appointment.getStatus())) {
                throw new SQLException("Appointment is already cancelled");
            }

            Connection conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false);
            
            try {
                String query = "UPDATE appointments SET status = 'CANCELLED' WHERE appointment_id = ?";
                
                try (PreparedStatement stmt = conn.prepareStatement(query)) {
                    stmt.setInt(1, appointmentId);
                    stmt.executeUpdate();
                }
                
                // Release resources
                resourceService.releaseResources(appointment);
                
                // Send cancellation notification
                Client client = clientService.getClientById(appointment.getClientId());
                notificationService.sendCancellationNotification(appointmentId, client.getEmail());
                
                // Log the action
                auditService.logAction("Appointment cancelled", "System");
                
                conn.commit();
                
                logger.info("Appointment cancelled successfully with ID: " + appointmentId);
            } catch (Exception e) {
                conn.rollback();
                throw e;
            } finally {
                conn.setAutoCommit(true);
                conn.close();
            }
        } catch (Exception e) {
            logger.severe("Failed to cancel appointment: " + e.getMessage());
            throw new SQLException("Failed to cancel appointment: " + e.getMessage(), e);
        }
    }

    public Appointment getAppointmentById(int appointmentId) throws SQLException {
        String query = "SELECT * FROM appointments WHERE appointment_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setInt(1, appointmentId);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return mapResultSetToAppointment(rs);
            }
            return null;
        } catch (SQLException e) {
            logger.severe("Failed to get appointment by ID: " + e.getMessage());
            throw e;
        }
    }

    public List<Appointment> getAppointmentsByClient(int clientId) throws SQLException {
        String query = "SELECT * FROM appointments WHERE client_id = ? ORDER BY start_time";
        List<Appointment> appointments = new ArrayList<>();
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setInt(1, clientId);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                appointments.add(mapResultSetToAppointment(rs));
            }
            
            logger.info("Retrieved " + appointments.size() + " appointments for client ID: " + clientId);
            return appointments;
        } catch (SQLException e) {
            logger.severe("Failed to get appointments by client: " + e.getMessage());
            throw e;
        }
    }

    private Appointment mapResultSetToAppointment(ResultSet rs) throws SQLException {
        Appointment appointment = new Appointment();
        appointment.setAppointmentId(rs.getInt("appointment_id"));
        appointment.setClientId(rs.getInt("client_id"));
        appointment.setPetId(rs.getInt("pet_id"));
        appointment.setServiceType(ServiceType.valueOf(rs.getString("service_type")));
        appointment.setStartTime(rs.getTimestamp("start_time"));
        appointment.setEndTime(rs.getTimestamp("end_time"));
        appointment.setStatus(rs.getString("status"));
        appointment.setPrice(rs.getDouble("price"));
        appointment.setNotes(rs.getString("notes"));
        return appointment;
    }
}