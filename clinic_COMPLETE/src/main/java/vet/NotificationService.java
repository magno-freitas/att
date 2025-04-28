package main.java.vet;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import main.java.vet.model.*;
import main.java.vet.service.EmailService;
import main.java.vet.service.PetService;
import main.java.vet.util.DatabaseConnection;
import main.java.vet.exception.NotificationException;

public class NotificationService {
    private static final Logger logger = Logger.getLogger(NotificationService.class.getName());
    private final EmailService emailService;
    private final SMSService smsService;
    
    public NotificationService(EmailService emailService, SMSService smsService) {
        this.emailService = emailService;
        this.smsService = smsService;
    }
    
    public void sendAppointmentReminder(Appointment appointment, Client client) throws NotificationException {
        try {
            String message = String.format(
                "Reminder: You have an appointment for %s on %s for pet %s.",
                appointment.getServiceType().name(),
                appointment.getStartTime(),
                getPetName(appointment.getPetId())
            );
            
            // Send notifications
            sendNotifications(client, "Appointment Reminder", message);
            
            // Log the notification
            logNotification(appointment.getAppointmentId(), "REMINDER", message);
            
            logger.info("Appointment reminder sent successfully for appointment ID: " + appointment.getAppointmentId());
        } catch (Exception e) {
            String errorMsg = "Failed to send appointment reminder: " + e.getMessage();
            logger.severe(errorMsg);
            throw new NotificationException(errorMsg, e);
        }
    }
    
    public void sendVaccinationDueReminder(Pet pet, Client client, String vaccineName) throws NotificationException {
        try {
            String message = String.format(
                "Hello! The %s vaccine for your pet %s is due soon. Please schedule a visit.",
                vaccineName,
                pet.getName()
            );
            
            sendNotifications(client, "Vaccination Reminder", message);
            logNotification(pet.getId(), "VACCINE_DUE", message);
            
            logger.info("Vaccination reminder sent successfully for pet ID: " + pet.getId());
        } catch (Exception e) {
            String errorMsg = "Failed to send vaccination reminder: " + e.getMessage();
            logger.severe(errorMsg);
            throw new NotificationException(errorMsg, e);
        }
    }
    
public void sendAppointmentConfirmation(Appointment appointment, Client client) {
        String message = String.format(
            "Olá %s, sua consulta foi agendada para %s",
            client.getName(),
            new SimpleDateFormat("dd/MM/yyyy HH:mm").format(appointment.getStartTime())
        );
        
        try {
            emailService.sendEmail(client.getEmail(), "Confirmação de Agendamento", message);
            smsService.sendSMS(client.getPhone(), message);
        } catch (Exception e) {
            logger.severe("Erro ao enviar notificação");
        }
    }

    
    public void sendCancellationNotification(int appointmentId, String email) throws NotificationException {
        try {
            String message = "Your appointment has been cancelled.";
            
            emailService.sendEmail(email, "Appointment Cancellation", message);
            logNotification(appointmentId, "CANCELLATION", message);
            
            logger.info("Cancellation notification sent successfully for appointment ID: " + appointmentId);
        } catch (Exception e) {
            String errorMsg = "Failed to send cancellation notification: " + e.getMessage();
            logger.severe(errorMsg);
            throw new NotificationException(errorMsg, e);
        }
    }
    
    private void sendNotifications(Client client, String subject, String message) throws NotificationException {
        try {
            // Try to send both email and SMS
            boolean emailSent = false;
            boolean smsSent = false;
            Exception lastError = null;
            
            try {
                emailService.sendEmail(client.getEmail(), subject, message);
                emailSent = true;
            } catch (Exception e) {
                lastError = e;
                logger.warning("Failed to send email: " + e.getMessage());
            }
            
            try {
                smsService.sendSMS(client.getPhone(), message);
                smsSent = true;
            } catch (Exception e) {
                lastError = e;
                logger.warning("Failed to send SMS: " + e.getMessage());
            }
            
            if (!emailSent && !smsSent) {
                throw new NotificationException("Failed to send both email and SMS notifications", lastError);
            }
        } catch (Exception e) {
            throw new NotificationException("Error sending notifications: " + e.getMessage(), e);
        }
    }
    
    private void logNotification(int referenceId, String type, String message) throws SQLException {
        String query = "INSERT INTO notification_log (reference_id, type, message, sent_at) VALUES (?, ?, ?, NOW())";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setInt(1, referenceId);
            stmt.setString(2, type);
            stmt.setString(3, message);
            
            stmt.executeUpdate();
            logger.info("Notification logged successfully for reference ID: " + referenceId);
        } catch (SQLException e) {
            logger.severe("Failed to log notification: " + e.getMessage());
            throw e;
        }
    }
    
    private String getPetName(int petId) throws SQLException {
        try {
            PetService petService = new PetService();
            Pet pet = petService.getPetById(petId);
            return pet != null ? pet.getName() : "Unknown";
        } catch (SQLException e) {
            logger.warning("Failed to get pet name for ID " + petId + ": " + e.getMessage());
            throw e;
        }
    }
    
    public List<NotificationLog> getRecentNotifications(int limit) throws SQLException {
        String query = "SELECT * FROM notification_log ORDER BY sent_at DESC LIMIT ?";
        List<NotificationLog> logs = new ArrayList<>();
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setInt(1, limit);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                NotificationLog log = new NotificationLog();
                log.setId(rs.getInt("id"));
                log.setReferenceId(rs.getInt("reference_id"));
                log.setType(rs.getString("type"));
                log.setMessage(rs.getString("message"));
                log.setSentAt(rs.getTimestamp("sent_at"));
                logs.add(log);
            }
            
            logger.info("Retrieved " + logs.size() + " recent notifications");
            return logs;
        } catch (SQLException e) {
            logger.severe("Failed to retrieve recent notifications: " + e.getMessage());
            throw e;
        }
        
    }
    
}
