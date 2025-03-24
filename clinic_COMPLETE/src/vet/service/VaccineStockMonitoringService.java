package vet.service;

import vet.*;
import java.sql.*;
import java.util.List;
import java.util.ArrayList;

public class VaccineStockMonitoringService {
    private final NotificationService notificationService;
    private final int LOW_STOCK_THRESHOLD = 10;

    public VaccineStockMonitoringService() {
        this.notificationService = new NotificationService();
    }

    public void checkStockLevels() throws SQLException {
        String query = "SELECT * FROM vaccine_stock WHERE quantity <= ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setInt(1, LOW_STOCK_THRESHOLD);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                notifyLowStock(rs.getString("vaccine_name"), rs.getInt("quantity"));
            }
        }
    }

    public void updateStock(int vaccineId, int quantity) throws SQLException {
        String query = "UPDATE vaccine_stock SET quantity = quantity - ? WHERE vaccine_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setInt(1, quantity);
            stmt.setInt(2, vaccineId);
            stmt.executeUpdate();
            
            // Check if we need to notify about low stock
            VaccineStock stock = getVaccineStock(vaccineId);
            if (stock.getQuantity() <= LOW_STOCK_THRESHOLD) {
                notifyLowStock(stock.getVaccineName(), stock.getQuantity());
            }
        }
    }

    private void notifyLowStock(String vaccineName, int currentQuantity) {
        String subject = "Alerta: Estoque Baixo de Vacina";
        String message = String.format("A vacina %s está com estoque baixo (quantidade atual: %d)",
            vaccineName, currentQuantity);
        EmailService.sendEmail("manager@vetclinic.com", subject, message);
    }

    private VaccineStock getVaccineStock(int vaccineId) throws SQLException {
        String query = "SELECT * FROM vaccine_stock WHERE vaccine_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setInt(1, vaccineId);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                VaccineStock stock = new VaccineStock();
                stock.setVaccineId(rs.getInt("vaccine_id"));
                stock.setVaccineName(rs.getString("vaccine_name"));
                stock.setQuantity(rs.getInt("quantity"));
                stock.setMinimumQuantity(rs.getInt("minimum_quantity"));
                return stock;
            }
        }
        return null;
    }
}