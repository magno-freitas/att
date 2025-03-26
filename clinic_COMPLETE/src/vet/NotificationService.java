package vet;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import javax.mail.Part;

import com.mysql.cj.xdevapi.Client;

import vet.model.Appointment;
import vet.service.EmailService;
import vet.util.DatabaseConnection;

public class NotificationService {
    private EmailService emailService;
    private SMSService smsService;
    
    public NotificationService() {
        this.emailService = new EmailService();
        this.smsService = new SMSService();
    }
    
    public NotificationService(EmailService emailService2, SMSService smsService2) {
        //TODO Auto-generated constructor stub
    }

    public void sendAppointmentReminder(Appointment appointment, Client client) {
        String message = String.format(
            "Lembrete: Você tem um agendamento para %s em %s para o pet %s.",
            appointment.getService(),
            appointment.getStartTime(),
            getPetName(appointment.getPetId())
        );
        
        // Send both email and SMS
        emailService.sendEmail(client.getEmail(), "Lembrete de Agendamento", message);
        smsService.sendSMS(client.getPhone(), message);
        
        // Log the notification
        logNotification(appointment.getAppointmentId(), "REMINDER", message);
    }
    
    public void sendVaccinationDueReminder(Part pet, Client client, String vaccineName) {
        String message = String.format(
            "Olá! A vacina %s do seu pet %s está próxima do vencimento. Por favor, agende uma visita.",
            vaccineName,
            pet.getFileName()
        );
        
        emailService.sendEmail(client.getEmail(), "Lembrete de Vacinação", message);
        smsService.sendSMS(client.getPhone(), message);
        
        logNotification(pet.getPetId(), "VACCINE_DUE", message);
    }
    
    public void sendAppointmentConfirmation(Appointment appointment, Client client) {
        String message = String.format(
            "Seu agendamento para %s em %s foi confirmado.",
            appointment.getService(),
            appointment.getStartTime()
        );
        
        emailService.sendEmail(client.getEmail(), "Confirmação de Agendamento", message);
        smsService.sendSMS(client.getPhone(), message);
        
        logNotification(appointment.getAppointmentId(), "CONFIRMATION", message);
    }
    
    public void sendCancellationNotification(Appointment appointment, Client client) {
        String message = String.format(
            "Seu agendamento para %s em %s foi cancelado.",
            appointment.getService(),
            appointment.getStartTime()
        );
        
        emailService.sendEmail(client.getEmail(), "Cancelamento de Agendamento", message);
        smsService.sendSMS(client.getPhone(), message);
        
        logNotification(appointment.getAppointmentId(), "CANCELLATION", message);
    }
    
    private void logNotification(int referenceId, String type, String message) {
        String query = "INSERT INTO notification_log (reference_id, type, message, sent_at) VALUES (?, ?, ?, NOW())";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setInt(1, referenceId);
            stmt.setString(2, type);
            stmt.setString(3, message);
            
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    private String getPetName(int petId) {
        try {
            PetService petService = new PetService();
            Pet pet = petService.getPetById(petId);
            return pet != null ? pet.getName() : "Unknown";
        } catch (SQLException e) {
            e.printStackTrace();
            return "Unknown";
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
        }
        
        return logs;
    }

    public <Pet> void sendVaccinationDueReminder(Pet pet, Client client, String vaccineName) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'sendVaccinationDueReminder'");
    }

    public <Client> void sendAppointmentConfirmation(Appointment appointment, Client client) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'sendAppointmentConfirmation'");
    }
}