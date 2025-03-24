package vet;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ResourceService {
    public void addResource(Resource resource) throws SQLException {
        String query = "INSERT INTO resources (name, type, availability) VALUES (?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
             
            stmt.setString(1, resource.getName());
            stmt.setString(2, resource.getType());
            stmt.setBoolean(3, resource.isAvailability());
            stmt.executeUpdate();
        }
    }
    
    public void updateResourceAvailability(int resourceId, boolean availability) throws SQLException {
        String query = "UPDATE resources SET availability = ? WHERE resource_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
             
            stmt.setBoolean(1, availability);
            stmt.setInt(2, resourceId);
            stmt.executeUpdate();
        }
    }

    public boolean isResourceAvailable(String resourceType, Timestamp startTime, int durationMinutes) throws SQLException {
        String sql = "SELECT COUNT(*) FROM resource_availability WHERE resource_type = ? " +
                    "AND start_time <= ? AND end_time >= ? AND status = 'AVAILABLE'";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, resourceType);
            stmt.setTimestamp(2, startTime);
            stmt.setTimestamp(3, Timestamp.valueOf(startTime.toLocalDateTime().plusMinutes(durationMinutes)));
            ResultSet rs = stmt.executeQuery();
            return rs.next() && rs.getInt(1) > 0;
        }
    }

    public Resource getResource(int resourceId) throws SQLException {
        String query = "SELECT * FROM resources WHERE resource_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
             
            stmt.setInt(1, resourceId);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                Resource resource = new Resource();
                resource.setResourceId(rs.getInt("resource_id"));
                resource.setName(rs.getString("name"));
                resource.setType(rs.getString("type"));
                resource.setAvailability(rs.getBoolean("availability"));
                return resource;
            }
        }
        
        return null;
    }
}
