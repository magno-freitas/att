package vet;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import vet.util.DatabaseConnection;

public class SupportService {
    public void createTicket(SupportTicket ticket) throws SQLException {
        String sql = "INSERT INTO support_tickets (user_id, subject, description, status, created_at) " +
                    "VALUES (?, ?, ?, 'OPEN', CURRENT_TIMESTAMP)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, ticket.getUserId());
            stmt.setString(2, ticket.getSubject());
            stmt.setString(3, ticket.getDescription());
            
            stmt.executeUpdate();
            
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    ticket.setTicketId(rs.getInt(1));
                }
            }
        }
    }

    public void updateTicketStatus(int ticketId, String status) throws SQLException {
        String sql = "UPDATE support_tickets SET status = ?, updated_at = CURRENT_TIMESTAMP WHERE ticket_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, status);
            stmt.setInt(2, ticketId);
            stmt.executeUpdate();
        }
    }

    public List<SupportTicket> getTicketsByUserId(int userId) throws SQLException {
        String sql = "SELECT * FROM support_tickets WHERE user_id = ?";
        List<SupportTicket> tickets = new ArrayList<>();
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                SupportTicket ticket = new SupportTicket();
                ticket.setTicketId(rs.getInt("ticket_id"));
                ticket.setUserId(rs.getInt("user_id"));
                ticket.setSubject(rs.getString("subject"));
                ticket.setDescription(rs.getString("description"));
                ticket.setStatus(rs.getString("status"));
                ticket.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
                ticket.setUpdatedAt(rs.getTimestamp("updated_at") != null ? 
                    rs.getTimestamp("updated_at").toLocalDateTime() : null);
                tickets.add(ticket);
            }
        }
        return tickets;
    }
}