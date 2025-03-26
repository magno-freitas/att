package vet.service;

import vet.*;
import vet.model.*;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class AppointmentService extends BaseService {
    @Override
    public String toString() {
        return "AppointmentService [using legacy implementation]";
    }
    private final NotificationService notificationService;
    private final AuditService auditService;
    private final ResourceService resourceService;
    private final PriceService priceService;
    private final ClientService clientService;
    private final PaymentGatewayService paymentGatewayService;
    private final AppointmentValidator validator;

    public AppointmentService(ServiceFactory serviceFactory) {
        super(serviceFactory);
        this.notificationService = serviceFactory.getNotificationService();
        this.auditService = serviceFactory.getAuditService();
        this.resourceService = serviceFactory.getResourceService();
        this.priceService = serviceFactory.getPriceService();
        this.clientService = serviceFactory.getClientService();
        this.validator = new AppointmentValidator(resourceService);
        this.paymentGatewayService = serviceFactory.getPaymentGatewayService();
    }

    public void scheduleAppointment(Appointment appointment) {
        if (appointment == null) {
            throw new ValidationException("Appointment cannot be null");
        }
        // Validate appointment
        validator.validateAppointment(appointment);
        
        // Set appointment end time based on service duration
        validator.setAppointmentEndTime(appointment);
        
        // Insert appointment into database
        String query = AppointmentQueries.INSERT_APPOINTMENT;
        
        try (Connection conn = ConnectionPool.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setInt(1, appointment.getClientId());
            stmt.setInt(2, appointment.getPetId());
            stmt.setString(3, appointment.getServiceType().name());
            stmt.setTimestamp(4, appointment.getStartTime());
            stmt.setTimestamp(5, appointment.getEndTime());
            stmt.setString(6, AppointmentStatus.SCHEDULED.toString());
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
            auditService.logAction("Appointment scheduled", "System");
        }
    }

    public void updateAppointment(Appointment appointment) {
        if (appointment == null || appointment.getAppointmentId() <= 0) {
            throw new ValidationException("Invalid appointment");
        }
        validator.validateAppointment(appointment);
        validator.setAppointmentEndTime(appointment);
        
        String query = AppointmentQueries.UPDATE_APPOINTMENT;
        
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
            auditService.logAction("Appointment updated", "System");
        }
    }

    public void cancelAppointment(int appointmentId) {
        Appointment appointment = getAppointmentById(appointmentId);
        if (appointment == null) {
            throw new AppointmentException("Appointment not found");
        }

        Client client = clientService.getClientById(appointment.getClientId());
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
        String query = AppointmentQueries.CANCEL_APPOINTMENT;
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setInt(1, appointmentId);
            stmt.executeUpdate();
            
            // Notification already sent using the client we looked up earlier
            
            // Send cancellation notification
            notificationService.sendCancellationNotification(appointmentId, client.getEmail());
            
            // Log the action
            auditService.logAction("Appointment cancelled: " + appointmentId, "System");
        }
    }

    public Appointment getAppointmentById(int appointmentId) {
        final Appointment[] result = new Appointment[1];
        
        TransactionManager.executeInTransaction(conn -> {
            try (PreparedStatement stmt = conn.prepareStatement(AppointmentQueries.GET_APPOINTMENT)) {
                stmt.setInt(1, appointmentId);
                
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        result[0] = new Appointment();
                        result[0].setAppointmentId(rs.getInt("appointment_id"));
                        result[0].setClientId(rs.getInt("client_id"));
                        result[0].setPetId(rs.getInt("pet_id"));
                        result[0].setServiceType(ServiceType.valueOf(rs.getString("service_type")));
                        result[0].setStartTime(rs.getTimestamp("start_time"));
                        result[0].setEndTime(rs.getTimestamp("end_time"));
                        result[0].setStatus(rs.getString("status"));
                        result[0].setPrice(rs.getDouble("price"));
                        result[0].setNotes(rs.getString("notes"));
                    }
                }
            }
        });
        
        return result[0];
    }
}