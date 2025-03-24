package vet.service;

import vet.*;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;

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
        if (appointment.getServiceType() == null) {
            throw new IllegalArgumentException("Tipo de serviço não pode ser nulo");
        }
        
        validateBusinessHours(appointment.getStartTime());
        validateTimeSlot(appointment);
        
        switch (appointment.getServiceType()) {
            case BANHO:
            case TOSA:
                validateGroomingAvailability(appointment);
                break;
            case VACINA:
                validateVaccineAvailability();
                validateVetAvailability(appointment);
                break;
            case CONSULTA:
                validateVetAvailability(appointment);
                validateConsultationRoomAvailability(appointment);
                break;
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