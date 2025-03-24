package vet;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReportingService {
    
    public List<Appointment> getDailyAppointments(Date date) throws SQLException {
        String query = "SELECT * FROM appointments WHERE DATE(start_time) = ? ORDER BY start_time";
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

    public Map<String, Integer> getVaccineInventoryReport() throws SQLException {
        String query = "SELECT vaccine_name, quantity FROM vaccine_stock WHERE quantity <= reorder_level";
        Map<String, Integer> lowStock = new HashMap<>();
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                lowStock.put(rs.getString("vaccine_name"), rs.getInt("quantity"));
            }
        }
        
        return lowStock;
    }

    public double getCancellationRate(Date startDate, Date endDate) throws SQLException {
        String query = "SELECT " +
                      "(SELECT COUNT(*) FROM appointments WHERE status = 'cancelado' AND start_time BETWEEN ? AND ?) * 100.0 / " +
                      "COUNT(*) as cancel_rate " +
                      "FROM appointments WHERE start_time BETWEEN ? AND ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setDate(1, startDate);
            stmt.setDate(2, endDate);
            stmt.setDate(3, startDate);
            stmt.setDate(4, endDate);
            
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return rs.getDouble("cancel_rate");
            }
        }
        
        return 0.0;
    }

    public Map<String, Double> getRevenueByService(Date startDate, Date endDate) throws SQLException {
        String query = "SELECT service_type, SUM(price) as total_revenue " +
                      "FROM appointments WHERE start_time BETWEEN ? AND ? " +
                      "GROUP BY service_type";
        
        Map<String, Double> revenue = new HashMap<>();
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setDate(1, startDate);
            stmt.setDate(2, endDate);
            
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                revenue.put(rs.getString("service_type"), rs.getDouble("total_revenue"));
            }
        }
        
        return revenue;
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
        appointment.setNotes(rs.getString("notes"));
        return appointment;
    }
}