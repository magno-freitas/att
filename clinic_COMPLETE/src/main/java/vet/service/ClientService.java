package main.java.vet.service;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import main.java.vet.model.Client;
import main.java.vet.util.DatabaseConnection;

public class ClientService {
    private static final Logger logger = Logger.getLogger(ClientService.class.getName());
    private final AuditService auditService;

    public ClientService(AuditService auditService) {
        this.auditService = auditService;
    }

    public void addClient(Client client) throws SQLException {
        String query = "INSERT INTO clients (name, email, phone, address) VALUES (?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setString(1, client.getName());
            stmt.setString(2, client.getEmail());
            stmt.setString(3, client.getPhone());
            stmt.setString(4, client.getAddress());
            
            stmt.executeUpdate();
            
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    client.setId(rs.getInt(1));
                }
            }
            
            auditService.logAction("clients", client.getId(), "INSERT", null, client.toString());
            logger.info("Client added successfully with ID: " + client.getId());
        } catch (SQLException e) {
            logger.severe("Failed to add client: " + e.getMessage());
            throw e;
        }
    }

    public Client getClientById(int clientId) throws SQLException {
        String query = "SELECT * FROM clients WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setInt(1, clientId);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                Client client = new Client();
                client.setId(rs.getInt("id"));
                client.setName(rs.getString("name"));
                client.setEmail(rs.getString("email"));
                client.setPhone(rs.getString("phone"));
                client.setAddress(rs.getString("address"));
                return client;
            }
            return null;
        } catch (SQLException e) {
            logger.severe("Failed to get client by ID: " + e.getMessage());
            throw e;
        }
    }

    public List<Client> getAllClients() throws SQLException {
        String query = "SELECT * FROM clients ORDER BY name";
        List<Client> clients = new ArrayList<>();
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                Client client = new Client();
                client.setId(rs.getInt("id"));
                client.setName(rs.getString("name"));
                client.setEmail(rs.getString("email"));
                client.setPhone(rs.getString("phone"));
                client.setAddress(rs.getString("address"));
                clients.add(client);
            }
            
            logger.info("Retrieved " + clients.size() + " clients");
            return clients;
        } catch (SQLException e) {
            logger.severe("Failed to get all clients: " + e.getMessage());
            throw e;
        }
    }

    public void updateClient(Client client) throws SQLException {
        String query = "UPDATE clients SET name = ?, email = ?, phone = ?, address = ? WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setString(1, client.getName());
            stmt.setString(2, client.getEmail());
            stmt.setString(3, client.getPhone());
            stmt.setString(4, client.getAddress());
            stmt.setInt(5, client.getId());
            
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected == 0) {
                throw new SQLException("Client update failed, no rows affected.");
            }
            
            auditService.logAction("clients", client.getId(), "UPDATE", null, client.toString());
            logger.info("Client updated successfully with ID: " + client.getId());
        } catch (SQLException e) {
            logger.severe("Failed to update client: " + e.getMessage());
            throw e;
        }
    }

    public void deleteClient(int clientId) throws SQLException {
        String query = "DELETE FROM clients WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setInt(1, clientId);
            
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected == 0) {
                throw new SQLException("Client deletion failed, no rows affected.");
            }
            
            auditService.logAction("clients", clientId, "DELETE", null, null);
            logger.info("Client deleted successfully with ID: " + clientId);
        } catch (SQLException e) {
            logger.severe("Failed to delete client: " + e.getMessage());
            throw e;
        }
    }
}