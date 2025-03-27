package vet.service;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import vet.DatabaseConnection;
import vet.model.AuditLog;

public class AuditService<AuditLog> {
    public void logAction(String tableName, int recordId, String action, String oldValue, String newValue) {
        String query = "INSERT INTO audit_trail (table_name, record_id, action, old_value, new_value, user_id) " +
                      "VALUES (?, ?, ?, ?, ?, ?)";
                      
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setString(1, tableName);
            stmt.setInt(2, recordId);
            stmt.setString(3, action);
            stmt.setString(4, oldValue);
            stmt.setString(5, newValue);
            stmt.setInt(6, getCurrentUserId());
            
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public <AuditLog> List<AuditLog> getAuditTrail(String tableName, int recordId) throws SQLException {
        String query = "SELECT * FROM audit_trail WHERE table_name = ? AND record_id = ? ORDER BY created_at DESC";
        List<AuditLog> logs = new ArrayList<>();
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setString(1, tableName);
            stmt.setInt(2, recordId);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                logs.add(mapResultSetToAuditLog(rs));
            }
        }
        
        return logs;
    }

    private int getCurrentUserId() {
        // In production, this would get the current user from a session or security context
        return 1; // Default system user
    }

    private AuditLog mapResultSetToAuditLog(ResultSet rs) throws SQLException {
        AuditLog log = new AuditLog();
        log.setId(rs.getInt("id"));
        log.setTableName(rs.getString("table_name"));
        log.setRecordId(rs.getInt("record_id"));
        log.setAction(rs.getString("action"));
        log.setOldValue(rs.getString("old_value"));
        log.setNewValue(rs.getString("new_value"));
        log.setUserId(rs.getInt("user_id"));
        log.setCreatedAt(rs.getTimestamp("created_at"));
        return log;
    }
}