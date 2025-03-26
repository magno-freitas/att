package vet.service;

import vet.model.*;
import vet.dao.*;
import vet.util.*;
import vet.exception.*;
import java.sql.*;
import java.math.BigDecimal;

public class AppointmentService extends BaseService {
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
        this.paymentGatewayService = serviceFactory.getPaymentGatewayService();
        this.validator = new AppointmentValidator(resourceService);
    }

    public void scheduleAppointment(Appointment appointment) {
        if (appointment == null) {
            throw new ValidationException("Appointment cannot be null");
        }
        validator.validateAppointment(appointment);
        validator.setAppointmentEndTime(appointment);

        TransactionManager.executeInTransaction(conn -> {
            try (PreparedStatement stmt = conn.prepareStatement(AppointmentQueries.INSERT_APPOINTMENT, Statement.RETURN_GENERATED_KEYS)) {
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

                Client client = clientService.getClientById(appointment.getClientId());
                notificationService.sendAppointmentConfirmation(
                    appointment.getAppointmentId(),
                    client.getEmail(),
                    appointment.getStartTime()
                );

                auditService.logAction("Appointment " + appointment.getAppointmentId() + " scheduled", "System");
            }
        });
    }

    public void updateAppointment(Appointment appointment) {
        if (appointment == null || appointment.getAppointmentId() <= 0) {
            throw new ValidationException("Invalid appointment");
        }
        validator.validateAppointment(appointment);
        validator.setAppointmentEndTime(appointment);

        TransactionManager.executeInTransaction(conn -> {
            try (PreparedStatement stmt = conn.prepareStatement(AppointmentQueries.UPDATE_APPOINTMENT)) {
                stmt.setString(1, appointment.getServiceType().name());
                stmt.setTimestamp(2, appointment.getStartTime());
                stmt.setTimestamp(3, appointment.getEndTime());
                stmt.setString(4, appointment.getStatus());
                stmt.setDouble(5, appointment.getPrice());
                stmt.setString(6, appointment.getNotes());
                stmt.setInt(7, appointment.getAppointmentId());

                int updated = stmt.executeUpdate();
                if (updated == 0) {
                    throw new AppointmentException("Appointment not found: " + appointment.getAppointmentId());
                }

                Client client = clientService.getClientById(appointment.getClientId());
                notificationService.sendAppointmentConfirmation(
                    appointment.getAppointmentId(),
                    client.getEmail(),
                    appointment.getStartTime()
                );

                auditService.logAction("Appointment " + appointment.getAppointmentId() + " updated", "System");
            }
        });
    }

    public void cancelAppointment(int appointmentId) {
        Appointment appointment = getAppointmentById(appointmentId);
        if (appointment == null) {
            throw new AppointmentException("Appointment not found: " + appointmentId);
        }

        Client client = clientService.getClientById(appointment.getClientId());

        // Handle cancellation fees if needed
        if (!CancellationPolicy.canCancel(appointment.getStartTime().toLocalDateTime())) {
            double cancellationFee = CancellationPolicy.calculateCancellationFee(
                appointment.getPrice(),
                appointment.getStartTime().toLocalDateTime()
            );

            if (cancellationFee > 0) {
                PaymentRequest paymentRequest = new PaymentRequest(
                    appointmentId,
                    BigDecimal.valueOf(cancellationFee),
                    "CANCELLATION_FEE"
                );
                paymentGatewayService.processPayment(paymentRequest);
            }
        }

        TransactionManager.executeInTransaction(conn -> {
            try (PreparedStatement stmt = conn.prepareStatement(AppointmentQueries.CANCEL_APPOINTMENT)) {
                stmt.setString(1, AppointmentStatus.CANCELLED.toString());
                stmt.setInt(2, appointmentId);

                int updated = stmt.executeUpdate();
                if (updated == 0) {
                    throw new AppointmentException("Failed to cancel appointment: " + appointmentId);
                }

                notificationService.sendCancellationNotification(appointmentId, client.getEmail());
                auditService.logAction("Appointment " + appointmentId + " cancelled", "System");
            }
        });
    }

    public Appointment getAppointmentById(int appointmentId) {
        if (appointmentId <= 0) {
            throw new ValidationException("Invalid appointment ID");
        }

        final Appointment[] result = new Appointment[1];

        TransactionManager.executeInTransaction(conn -> {
            try (PreparedStatement stmt = conn.prepareStatement(AppointmentQueries.GET_APPOINTMENT)) {
                stmt.setInt(1, appointmentId);

                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        result[0] = mapResultSetToAppointment(rs);
                    }
                }
            }
        });

        return result[0];
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