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

import com.mysql.cj.xdevapi.Client;

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
        
            private Appointment getAppointmentById(int appointmentId) {
                // TODO Auto-generated method stub
                throw new UnsupportedOperationException("Unimplemented method 'getAppointmentById'");
            }

    // Other existing methods...
}