package vet.audit;

import vet.*;
import vet.util.DatabaseConnection;

import java.sql.*;
import java.time.LocalDateTime;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

public class AuditTrailService {
    public void logAccess(String username, String action, String resource, String status, String ipAddress) throws SQLException {
        String sql = "INSERT INTO audit_trail (username, action, resource, status, ip_address, access_time) " +
                    "VALUES (?, ?, ?, ?, ?, CURRENT_TIMESTAMP)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            stmt.setString(2, action);
            stmt.setString(3, resource);
            stmt.setString(4, status);
            stmt.setString(5, ipAddress);
            stmt.executeUpdate();
        }
    }

    public void logDataAccess(String username, String dataType, int recordId, String action) throws SQLException {
        String sql = "INSERT INTO data_access_log (username, data_type, record_id, action, access_time) " +
                    "VALUES (?, ?, ?, ?, CURRENT_TIMESTAMP)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            stmt.setString(2, dataType);
            stmt.setInt(3, recordId);
            stmt.setString(4, action);
            stmt.executeUpdate();
        }
    }

    public void logFailedLogin(String username, String ipAddress, String reason) throws SQLException {
        String sql = "INSERT INTO failed_login_attempts (username, ip_address, reason, attempt_time) " +
                    "VALUES (?, ?, ?, CURRENT_TIMESTAMP)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            stmt.setString(2, ipAddress);
            stmt.setString(3, reason);
            stmt.executeUpdate();
        }
    }

    public List<AuditEntry> getAuditTrail(LocalDateTime startDate, LocalDateTime endDate, String username) throws SQLException {
        String sql = "SELECT * FROM audit_trail WHERE access_time BETWEEN ? AND ? " +
                    (username != null ? "AND username = ? " : "") +
                    "ORDER BY access_time DESC";
        
        List<AuditEntry> entries = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setTimestamp(1, Timestamp.valueOf(startDate));
            stmt.setTimestamp(2, Timestamp.valueOf(endDate));
            if (username != null) {
                stmt.setString(3, username);
            }
            
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                AuditEntry entry = new AuditEntry();
                entry.setId(rs.getInt("id"));
                entry.setUsername(rs.getString("username"));
                entry.setAction(rs.getString("action"));
                entry.setResource(rs.getString("resource"));
                entry.setStatus(rs.getString("status"));
                entry.setIpAddress(rs.getString("ip_address"));
                entry.setAccessTime(rs.getTimestamp("access_time").toLocalDateTime());
                entries.add(entry);
            }
        }
        return entries;
    }

    public boolean isUserLocked(String username, String ipAddress) throws SQLException {
        String sql = "SELECT COUNT(*) FROM failed_login_attempts " +
                    "WHERE (username = ? OR ip_address = ?) " +
                    "AND attempt_time > DATE_SUB(NOW(), INTERVAL 30 MINUTE)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            stmt.setString(2, ipAddress);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1) >= 5; // Lock after 5 failed attempts
            }
        }
        return false;
    }
}