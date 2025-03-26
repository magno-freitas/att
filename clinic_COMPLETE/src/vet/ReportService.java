package vet;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import vet.util.DatabaseConnection;

public class ReportService {
    public Map<String, Integer> generateActivityReport(LocalDateTime startDate, LocalDateTime endDate) throws SQLException {
        Map<String, Integer> report = new HashMap<>();
        
        String sql = "SELECT status, COUNT(*) as count FROM appointments " +
                    "WHERE start_time BETWEEN ? AND ? " +
                    "GROUP BY status";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setTimestamp(1, Timestamp.valueOf(startDate));
            stmt.setTimestamp(2, Timestamp.valueOf(endDate));
            
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                report.put(rs.getString("status"), rs.getInt("count"));
            }
        }
        return report;
    }

    public List<Map<String, Object>> generateServiceReport(LocalDateTime startDate, LocalDateTime endDate) throws SQLException {
        List<Map<String, Object>> report = new ArrayList<>();
        
        String sql = "SELECT service_type, COUNT(*) as count, AVG(rating) as avg_rating " +
                    "FROM appointments a " +
                    "LEFT JOIN feedback f ON a.appointment_id = f.appointment_id " +
                    "WHERE start_time BETWEEN ? AND ? " +
                    "GROUP BY service_type";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setTimestamp(1, Timestamp.valueOf(startDate));
            stmt.setTimestamp(2, Timestamp.valueOf(endDate));
            
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Map<String, Object> serviceStats = new HashMap<>();
                serviceStats.put("service_type", rs.getString("service_type"));
                serviceStats.put("count", rs.getInt("count"));
                serviceStats.put("average_rating", rs.getDouble("avg_rating"));
                report.add(serviceStats);
            }
        }
        return report;
    }

    public Map<String, Double> generateRevenueReport(LocalDateTime startDate, LocalDateTime endDate) throws SQLException {
        Map<String, Double> report = new HashMap<>();
        
        String sql = "SELECT service_type, SUM(amount) as total_revenue " +
                    "FROM appointments a " +
                    "JOIN payments p ON a.appointment_id = p.appointment_id " +
                    "WHERE start_time BETWEEN ? AND ? " +
                    "GROUP BY service_type";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setTimestamp(1, Timestamp.valueOf(startDate));
            stmt.setTimestamp(2, Timestamp.valueOf(endDate));
            
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                report.put(rs.getString("service_type"), rs.getDouble("total_revenue"));
            }
        }
        return report;
    }
}