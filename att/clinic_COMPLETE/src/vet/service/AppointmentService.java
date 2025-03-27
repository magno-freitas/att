package vet.service;

import vet.dao.ConnectionPool;
import vet.exception.ServiceException;
import vet.exception.ValidationException;
import vet.model.Appointment;
import vet.model.Client;
import vet.model.ServiceType;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AppointmentService {
    private final AppointmentValidator validator;
    private final NotificationService notificationService;
    private final AuditService auditService;
    private final ResourceService resourceService;
    private final ServiceFactory serviceFactory;
    private final ClientService clientService;

    public AppointmentService(ServiceFactory serviceFactory) {
        this.serviceFactory = serviceFactory;
        this.resourceService = serviceFactory.getResourceService();
        this.validator = new AppointmentValidator(resourceService);
        this.notificationService = serviceFactory.getNotificationService();
        this.auditService = serviceFactory.getAuditService();
        this.clientService = serviceFactory.getClientService();
    }

    public void scheduleAppointment(Appointment appointment) {
        try {
            validator.validateAppointment(appointment);
            validator.setAppointmentEndTime(appointment);
            
            try (Connection conn = ConnectionPool.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(
                     "INSERT INTO appointments (client_id, pet_id, service_type, start_time, end_time, status, price, notes) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS)) {
                
                stmt.setInt(1, appointment.getClientId());
                stmt.setInt(2, appointment.getPetId());
                stmt.setString(3, appointment.getServiceType().name());
                stmt.setTimestamp(4, appointment.getStartTime());
                stmt.setTimestamp(5, appointment.getEndTime());
                stmt.setString(6, appointment.getStatus());
                stmt.setDouble(7, appointment.getPrice());
                stmt.setString(8, appointment.getNotes());
                
                stmt.executeUpdate();
                
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        appointment.setAppointmentId(generatedKeys.getInt(1));
                    }
                }
                
                Client client = clientService.getClientById(appointment.getClientId());
                notificationService.sendAppointmentConfirmation(
                    appointment.getAppointmentId(),
                    client.getEmail(),
                    appointment.getStartTime()
                );
                
                auditService.logAction("Appointment scheduled", "System");
            }
        } catch (SQLException e) {
            throw new ServiceException("Failed to schedule appointment", e);
        } catch (ValidationException e) {
            throw new ServiceException("Invalid appointment data: " + e.getMessage(), e);
        }
    }

    public void updateAppointment(Appointment appointment) {
        try {
            validator.validateAppointment(appointment);
            validator.setAppointmentEndTime(appointment);
            
            try (Connection conn = ConnectionPool.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(
                     "UPDATE appointments SET service_type = ?, start_time = ?, end_time = ?, " +
                     "status = ?, price = ?, notes = ? WHERE appointment_id = ?")) {
                
                stmt.setString(1, appointment.getServiceType().name());
                stmt.setTimestamp(2, appointment.getStartTime());
                stmt.setTimestamp(3, appointment.getEndTime());
                stmt.setString(4, appointment.getStatus());
                stmt.setDouble(5, appointment.getPrice());
                stmt.setString(6, appointment.getNotes());
                stmt.setInt(7, appointment.getAppointmentId());
                
                int rowsUpdated = stmt.executeUpdate();
                if (rowsUpdated == 0) {
                    throw new ServiceException("Appointment not found: " + appointment.getAppointmentId());
                }
                
                Client client = clientService.getClientById(appointment.getClientId());
                notificationService.sendAppointmentConfirmation(
                    appointment.getAppointmentId(),
                    client.getEmail(),
                    appointment.getStartTime()
                );
                
                auditService.logAction("Appointment updated", "System");
            }
        } catch (SQLException e) {
            throw new ServiceException("Failed to update appointment", e);
        } catch (ValidationException e) {
            throw new ServiceException("Invalid appointment data: " + e.getMessage(), e);
        }
    }

    public void cancelAppointment(int appointmentId) {
        try {
            Appointment appointment = getAppointmentById(appointmentId);
            if (appointment == null) {
                throw new ServiceException("Appointment not found: " + appointmentId);
            }
            
            try (Connection conn = ConnectionPool.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(
                     "UPDATE appointments SET status = 'CANCELLED' WHERE appointment_id = ?")) {
                
                stmt.setInt(1, appointmentId);
                stmt.executeUpdate();
                
                Client client = clientService.getClientById(appointment.getClientId());
                notificationService.sendCancellationNotification(
                    appointmentId,
                    client.getEmail()
                );
                
                auditService.logAction("Appointment cancelled", "System");
            }
        } catch (SQLException e) {
            throw new ServiceException("Failed to cancel appointment", e);
        }
    }

    private Appointment getAppointmentById(int appointmentId) {
        try (Connection conn = ConnectionPool.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                 "SELECT * FROM appointments WHERE appointment_id = ?")) {
            
            stmt.setInt(1, appointmentId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToAppointment(rs);
                }
                return null;
            }
        } catch (SQLException e) {
            throw new ServiceException("Failed to retrieve appointment", e);
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