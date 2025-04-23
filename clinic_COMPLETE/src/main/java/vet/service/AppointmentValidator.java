package main.java.vet.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;

import main.java.vet.ResourceService;
import main.java.vet.model.Appointment;
import main.java.vet.model.ServiceType;
import main.java.vet.util.DatabaseConnection;

public class AppointmentValidator {
    private final ResourceService resourceService;
    
    public AppointmentValidator(ResourceService resourceService) {
        this.resourceService = resourceService;
    }

    public void validateGroomingAvailability(Appointment appointment) throws SQLException {
        if (!resourceService.isResourceAvailable("GROOMING_STATION", 
                appointment.getStartTime(), 
                getServiceDuration(appointment.getServiceType()))) {
            throw new SQLException("Estação de banho e tosa não disponível neste horário");
        }
    }

    public void validateConsultationRoomAvailability(Appointment appointment) throws SQLException {
        if (!resourceService.isResourceAvailable("CONSULTATION_ROOM", 
                appointment.getStartTime(), 
                getServiceDuration(appointment.getServiceType()))) {
            throw new SQLException("Sala de consulta não disponível neste horário");
        }
    }

    public void validateVetAvailability(Appointment appointment) throws SQLException {
        if (!resourceService.isResourceAvailable("VETERINARIAN", 
                appointment.getStartTime(), 
                getServiceDuration(appointment.getServiceType()))) {
            throw new SQLException("Não há veterinários disponíveis neste horário");
        }
    }

    public void setAppointmentEndTime(Appointment appointment) {
        int duration = getServiceDuration(appointment.getServiceType());
        LocalDateTime endTime = appointment.getStartTime().toLocalDateTime().plusMinutes(duration);
        appointment.setEndTime(Timestamp.valueOf(endTime));
    }

    public void validateAppointment(Appointment appointment) throws SQLException {
        // Basic validation
        if (appointment == null) {
            throw new IllegalArgumentException("Appointment cannot be null");
        }
        if (appointment.getServiceType() == null) {
            throw new IllegalArgumentException("Service type cannot be null");
        }
        if (appointment.getStartTime() == null) {
            throw new IllegalArgumentException("Start time cannot be null");
        }
        if (appointment.getClientId() <= 0) {
            throw new IllegalArgumentException("Invalid client ID");
        }
        if (appointment.getPetId() <= 0) {
            throw new IllegalArgumentException("Invalid pet ID");
        }
        
        // Validate business hours
        validateBusinessHours(appointment.getStartTime());
        
        // Validate that start time is in the future
        LocalDateTime now = LocalDateTime.now();
        if (appointment.getStartTime().toLocalDateTime().isBefore(now)) {
            throw new IllegalArgumentException("Appointment cannot be scheduled in the past");
        }
        
        // Validate time slot availability
        validateTimeSlot(appointment);
        
        // Validate specific service requirements
        switch (appointment.getServiceType()) {
            case BANHO:
            case TOSA:
                validateGroomingAvailability(appointment);
                break;
            case VACINA:
                validateVaccineAvailability();
                validateVetAvailability(appointment);
                validateVaccinationHistory(appointment);
                break;
            case CONSULTA:
                validateVetAvailability(appointment);
                validateConsultationRoomAvailability(appointment);
                validatePetHealthStatus(appointment);
                break;
            default:
                throw new IllegalArgumentException("Invalid service type");
        }
    }

    private void validatePetHealthStatus(Appointment appointment) throws SQLException {
        // Check if pet has any critical health conditions that need special attention
        String query = "SELECT health_status FROM pet_health_records WHERE pet_id = ? ORDER BY record_date DESC LIMIT 1";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, appointment.getPetId());
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                String healthStatus = rs.getString("health_status");
                if ("CRITICAL".equals(healthStatus)) {
                    throw new SQLException("Pet requires special medical attention. Please contact the clinic.");
                }
            }
        }
    }

    private void validateVaccinationHistory(Appointment appointment) throws SQLException {
        // Check if pet has any contraindications for vaccination
        String query = "SELECT reaction_history FROM vaccine_records WHERE pet_id = ? AND reaction_history IS NOT NULL";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, appointment.getPetId());
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                String reactionHistory = rs.getString("reaction_history");
                if (reactionHistory != null && !reactionHistory.isEmpty()) {
                    throw new SQLException("Pet has history of vaccine reactions. Special consultation required.");
                }
            }
        }
    }

    private void validateBusinessHours(Timestamp startTime) {
        int hour = startTime.toLocalDateTime().getHour();
        if (hour < 8 || hour >= 18) {
            throw new IllegalArgumentException("Horário fora do período de funcionamento (8h-18h)");
        }
    }

    private void validateTimeSlot(Appointment appointment) throws SQLException {
        String query = "SELECT COUNT(*) FROM appointments WHERE start_time = ? AND status != 'CANCELLED'";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setTimestamp(1, appointment.getStartTime());
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next() && rs.getInt(1) >= 3) {
                throw new SQLException("Horário já está totalmente ocupado");
            }
        }
    }

    private void validateVaccineAvailability() throws SQLException {
        String query = "SELECT COUNT(*) FROM vaccine_stock WHERE quantity > 0";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            ResultSet rs = stmt.executeQuery();
            if (!rs.next() || rs.getInt(1) == 0) {
                throw new SQLException("Não há vacinas disponíveis no momento");
            }
        }
    }

    private int getServiceDuration(ServiceType serviceType) {
        switch (serviceType) {
            case CONSULTA:
                return 30; // 30 minutes for consultation
            case VACINA:
                return 15; // 15 minutes for vaccination
            case BANHO:
                return 60; // 60 minutes for bath
            case TOSA:
                return 90; // 90 minutes for grooming
            default:
                return 30; // default duration
        }
    }
}
