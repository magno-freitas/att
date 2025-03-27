package vet.service;

import vet.*;
import vet.model.*;
import vet.payment.PaymentRequest;
import vet.util.DatabaseConnection;

import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import vet.service.ClientService;
import vet.model.Client;

public class AppointmentService<AuditLogService> {
    private final AppointmentValidator validator;
    private final NotificationService notificationService;
    private final AuditLogService auditLogService;
    private final ResourceService resourceService;

    public AppointmentService() {
        this.resourceService = new ResourceService();
        this.validator = new AppointmentValidator(resourceService);
        this.notificationService = new NotificationService();
        this.auditLogService = new AuditLogService();
    }

    public <Client> void scheduleAppointment(Appointment appointment) throws SQLException {
        // Validate appointment
        validator.validateAppointment(appointment);
        
        // Set appointment end time based on service duration
        validator.setAppointmentEndTime(appointment);
        
        // Insert appointment into database
        String query = "INSERT INTO appointments (client_id, pet_id, service_type, start_time, end_time, status, price, notes) " +
                      "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            
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
            
            // Send confirmation email
            Client client = new ClientService().getClientById(appointment.getClientId());
            notificationService.sendAppointmentConfirmation(
                appointment.getAppointmentId(),
                client.getEmail(),
                appointment.getStartTime()
            );
            
            // Log the action
            auditLogService.logAction("Appointment scheduled", "System");
        }
    }

    public void updateAppointment(Appointment appointment) throws SQLException {
        validator.validateAppointment(appointment);
        validator.setAppointmentEndTime(appointment);
        
        String query = "UPDATE appointments SET service_type = ?, start_time = ?, end_time = ?, " +
                      "status = ?, price = ?, notes = ? WHERE appointment_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setString(1, appointment.getServiceType().name());
            stmt.setTimestamp(2, appointment.getStartTime());
            stmt.setTimestamp(3, appointment.getEndTime());
            stmt.setString(4, appointment.getStatus());
            stmt.setDouble(5, appointment.getPrice());
            stmt.setString(6, appointment.getNotes());
            stmt.setInt(7, appointment.getAppointmentId());
            
            stmt.executeUpdate();
            
            // Send update notification
            Client client = new ClientService().getClientById(appointment.getClientId());
            notificationService.sendAppointmentConfirmation(
                appointment.getAppointmentId(),
                client.getEmail(),
                appointment.getStartTime()
            );
            
            // Log the action
            auditLogService.logAction("Appointment updated", "System");
        }
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        // TODO Auto-generated method stub
        return super.clone();
    }

    @Override
    public boolean equals(Object obj) {
        // TODO Auto-generated method stub
        return super.equals(obj);
    }

    @Override
    protected void finalize() throws Throwable {
        // TODO Auto-generated method stub
        super.finalize();
    }

    @Override
    public int hashCode() {
        // TODO Auto-generated method stub
        return super.hashCode();
    }

    @Override
    public String toString() {
        // TODO Auto-generated method stub
        return super.toString();
    }

    public void cancelAppointment(int appointmentId, Object paymentGatewayService) throws SQLException {
            // Get appointment details
            Appointment appointment = getAppointmentById(appointmentId);
                    if (appointment == null) {
                        throw new SQLException("Appointment not found");
                    }
            
                    // Check cancellation policy
                    if (!CancellationPolicy.canCancel(appointment.getStartTime().toLocalDateTime())) {
                        double cancellationFee = CancellationPolicy.calculateCancellationFee(
                            appointment.getPrice(), 
                            appointment.getStartTime().toLocalDateTime()
                        );
                        
                        // Create payment request for cancellation fee
                        if (cancellationFee > 0) {
                            PaymentRequest paymentRequest = new PaymentRequest(
                                appointmentId,
                                BigDecimal.valueOf(cancellationFee),
                                "CANCELLATION_FEE"
                            );
                            paymentGatewayService.processPayment(paymentRequest);
                    }
                }
                String query = "UPDATE appointments SET status = 'CANCELLED' WHERE appointment_id = ?";
                
                try (Connection conn = DatabaseConnection.getConnection();
                     PreparedStatement stmt = conn.prepareStatement(query)) {
                    
                    stmt.setInt(1, appointmentId);
                    stmt.executeUpdate();
                    
                    // Get appointment details for notification
                    Appointment appointment = getAppointmentById(appointmentId);
                    Client client = new ClientService().getClientById(appointment.getClientId());
                    
                    // Send cancellation notification
                    notificationService.sendCancellationNotification(appointmentId, client.getEmail());
                    
                    // Log the action
                    auditLogService.logAction("Appointment cancelled", "System");
                }
            }
        
            private Appointment getAppointmentById(int appointmentId) throws SQLException {
                String query = "SELECT * FROM appointments WHERE appointment_id = ?";
                
                try (Connection conn = DatabaseConnection.getConnection();
                     PreparedStatement stmt = conn.prepareStatement(query)) {
                    
                    stmt.setInt(1, appointmentId);
                    ResultSet rs = stmt.executeQuery();
                    
                    if (rs.next()) {
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
                    return null;
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
                        appointments.add(appointment);
                    }
                }
                return appointments;
            }
        
            public List<Appointment> getAppointmentsByDate(LocalDateTime date) throws SQLException {
                String query = "SELECT * FROM appointments WHERE DATE(start_time) = DATE(?) ORDER BY start_time";
                List<Appointment> appointments = new ArrayList<>();
                
                try (Connection conn = DatabaseConnection.getConnection();
                     PreparedStatement stmt = conn.prepareStatement(query)) {
                    
                    stmt.setTimestamp(1, Timestamp.valueOf(date));
                    ResultSet rs = stmt.executeQuery();
                    
                    while (rs.next()) {
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
                        appointments.add(appointment);
                    }
                }
                return appointments;
            }
        
            public void completeAppointment(int appointmentId, String notes) throws SQLException {
                String query = "UPDATE appointments SET status = 'COMPLETED', notes = ? WHERE appointment_id = ?";
                
                try (Connection conn = DatabaseConnection.getConnection();
                     PreparedStatement stmt = conn.prepareStatement(query)) {
                    
                    stmt.setString(1, notes);
                    stmt.setInt(2, appointmentId);
                    stmt.executeUpdate();
                    
                    // Get appointment details for notification
                    Appointment appointment = getAppointmentById(appointmentId);
                    Client client = new ClientService().getClientById(appointment.getClientId());
                    
                    // Send completion notification
                    notificationService.sendCompletionNotification(appointmentId, client.getEmail());
                    
                    // Log the action
                    auditLogService.logAction("Appointment completed", "System");
                }
            }
        
            public List<Appointment> getUpcomingAppointments(int clientId) throws SQLException {
                String query = "SELECT * FROM appointments WHERE client_id = ? AND start_time > NOW() ORDER BY start_time";
                List<Appointment> appointments = new ArrayList<>();
                
                try (Connection conn = DatabaseConnection.getConnection();
                     PreparedStatement stmt = conn.prepareStatement(query)) {
                    
                    stmt.setInt(1, clientId);
                    ResultSet rs = stmt.executeQuery();
                    
                    while (rs.next()) {
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
                        appointments.add(appointment);
                    }
                }
                return appointments;
            }
        
            public boolean isTimeSlotAvailable(LocalDateTime startTime, ServiceType serviceType) throws SQLException {
                // Calculate end time based on service duration
                LocalDateTime endTime = startTime.plusMinutes(serviceType.getDuration());
                
                String query = "SELECT COUNT(*) FROM appointments WHERE " +
                              "((start_time BETWEEN ? AND ?) OR (end_time BETWEEN ? AND ?)) " +
                              "AND status != 'CANCELLED'";
                
                try (Connection conn = DatabaseConnection.getConnection();
                     PreparedStatement stmt = conn.prepareStatement(query)) {
                    
                    stmt.setTimestamp(1, Timestamp.valueOf(startTime));
                    stmt.setTimestamp(2, Timestamp.valueOf(endTime));
                    stmt.setTimestamp(3, Timestamp.valueOf(startTime));
                    stmt.setTimestamp(4, Timestamp.valueOf(endTime));
                    
                    ResultSet rs = stmt.executeQuery();
                    if (rs.next()) {
                        return rs.getInt(1) == 0;
                    }
                }
                return false;
            }
        }