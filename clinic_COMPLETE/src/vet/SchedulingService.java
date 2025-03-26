package vet;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import vet.model.Appointment;
import vet.util.DatabaseConnection;

public class SchedulingService {
    private static final int MINUTES_PER_SLOT = 30;
    private static final int MAX_SIMULTANEOUS_APPOINTMENTS = 3;
    private static final int MAX_SIMULTANEOUS_CONSULTATIONS = 2;
    
    public List<TimeSlot> getAvailableTimeSlots(Date date, ServiceType serviceType) throws SQLException {
        List<TimeSlot> availableSlots = new ArrayList<>();
        List<Appointment> existingAppointments = getAppointmentsForDate(date);
        
        // Generate time slots for business hours (8:00 to 18:00)
        Time startTime = Time.valueOf("08:00:00");
        Time endTime = Time.valueOf("18:00:00");
        
        long currentTime = startTime.getTime();
        long endTimeMillis = endTime.getTime();
        
        while (currentTime < endTimeMillis) {
            Timestamp slotTime = new Timestamp(date.getTime() + currentTime - startTime.getTime());
            
            if (isSlotAvailable(slotTime, serviceType, existingAppointments)) {
                TimeSlot slot = new TimeSlot();
                slot.setStartTime(slotTime);
                slot.setEndTime(new Timestamp(slotTime.getTime() + MINUTES_PER_SLOT * 60 * 1000));
                availableSlots.add(slot);
            }
            
            currentTime += MINUTES_PER_SLOT * 60 * 1000; // Add 30 minutes
        }
        
        return availableSlots;
    }
    
    private boolean isSlotAvailable(Timestamp time, ServiceType serviceType, List<Appointment> existingAppointments) {
        int simultaneousAppointments = 0;
        int simultaneousConsultations = 0;
        
        for (Appointment appointment : existingAppointments) {
            if (appointment.getStatus().equals("cancelado")) {
                continue;
            }
            
            if (appointment.getStartTime().equals(time)) {
                simultaneousAppointments++;
                
                if (appointment.getServiceType() == ServiceType.CONSULTA) {
                    simultaneousConsultations++;
                }
            }
        }
        
        if (simultaneousAppointments >= MAX_SIMULTANEOUS_APPOINTMENTS) {
            return false;
        }
        
        if (serviceType == ServiceType.CONSULTA && simultaneousConsultations >= MAX_SIMULTANEOUS_CONSULTATIONS) {
            return false;
        }
        
        return true;
    }
    
    private List<Appointment> getAppointmentsForDate(Date date) throws SQLException {
        String query = "SELECT * FROM appointments WHERE DATE(start_time) = ?";
        List<Appointment> appointments = new ArrayList<>();
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setDate(1, date);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                appointments.add(mapResultSetToAppointment(rs));
            }
        }
        
        return appointments;
    }
    
    private Appointment mapResultSetToAppointment(ResultSet rs) throws SQLException {
        Appointment appointment = new Appointment();
        appointment.setAppointmentId(rs.getInt("appointment_id"));
        appointment.setPetId(rs.getInt("pet_id"));
        appointment.setService(rs.getString("service"));
        appointment.setServiceType(ServiceType.valueOf(rs.getString("service_type")));
        appointment.setStartTime(rs.getTimestamp("start_time"));
        appointment.setEndTime(rs.getTimestamp("end_time"));
        appointment.setStatus(rs.getString("status"));
        return appointment;
    }
}