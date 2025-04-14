package main.java.vet.service;

import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import main.java.vet.*;
import main.java.vet.model.*;
import main.java.vet.payment.PaymentRequest;
import main.java.vet.util.DatabaseConnection;

public class AppointmentService {
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

    public void scheduleAppointment(Appointment appointment) throws SQLException {
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

    public void cancelAppointment(int appointmentId, PaymentGatewayService paymentGatewayService) throws SQLException {
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
            
            // Process cancellation fee if applicable
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
            
            Client client = new ClientService().getClientById(appointment.getClientId());
            notificationService.sendCancellationNotification(appointmentId, client.getEmail());
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
        Appointment appointment = getAppointmentById(appointmentId);
        if (appointment == null) {
            throw new SQLException("Appointment not found");
        }

        if ("CANCELLED".equals(appointment.getStatus())) {
            throw new SQLException("Cannot complete a cancelled appointment");
        }

        if ("COMPLETED".equals(appointment.getStatus())) {
            throw new SQLException("Appointment is already completed");
        }

        String query = "UPDATE appointments SET status = 'COMPLETED', notes = ? WHERE appointment_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setString(1, notes);
            stmt.setInt(2, appointmentId);
            stmt.executeUpdate();
            
            Client client = new ClientService().getClientById(appointment.getClientId());
            notificationService.sendCompletionNotification(appointmentId, client.getEmail());
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