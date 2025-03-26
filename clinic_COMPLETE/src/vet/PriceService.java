package vet;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;

import vet.model.Appointment;
import vet.util.DatabaseConnection;

public class PriceService {
    public void setServicePrice(ServiceType serviceType, double price) throws SQLException {
        String query = "INSERT INTO service_prices (service_type, price) VALUES (?, ?) " +
                      "ON DUPLICATE KEY UPDATE price = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setString(1, serviceType.name());
            stmt.setDouble(2, price);
            stmt.setDouble(3, price);
            
            stmt.executeUpdate();
        }
    }
    
    public double getServicePrice(ServiceType serviceType) throws SQLException {
        String query = "SELECT price FROM service_prices WHERE service_type = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setString(1, serviceType.name());
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return rs.getDouble("price");
            }
        }
        
        // Return default prices if not set
        switch (serviceType) {
            case BANHO: return 50.0;
            case TOSA: return 70.0;
            case VACINA: return 100.0;
            case CONSULTA: return 150.0;
            default: return 0.0;
        }
    }
    
    public Map<ServiceType, Double> getAllPrices() throws SQLException {
        String query = "SELECT service_type, price FROM service_prices";
        Map<ServiceType, Double> prices = new HashMap<>();
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            
            while (rs.next()) {
                ServiceType serviceType = ServiceType.valueOf(rs.getString("service_type"));
                double price = rs.getDouble("price");
                prices.put(serviceType, price);
            }
        }
        
        // Add default prices for any missing service types
        for (ServiceType type : ServiceType.values()) {
            if (!prices.containsKey(type)) {
                prices.put(type, getServicePrice(type));
            }
        }
        
        return prices;
    }
    
    public void updateAppointmentPrice(Appointment appointment) throws SQLException {
        double price = getServicePrice(appointment.getServiceType());
        appointment.setPrice(price);
        
        String query = "UPDATE appointments SET price = ? WHERE appointment_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setDouble(1, price);
            stmt.setInt(2, appointment.getAppointmentId());
            
            stmt.executeUpdate();
        }
    }
}