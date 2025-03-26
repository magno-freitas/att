package vet.service;

import vet.*;
import vet.model.*;
import vet.model.ServiceType;
import vet.util.DatabaseConnection;

import java.sql.*;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class PriceService {
    private Map<String, ServiceConfig> serviceConfigs;

    public PriceService() {
        loadServiceConfigurations();
    }

    private void loadServiceConfigurations() {
        serviceConfigs = new HashMap<>();
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT * FROM service_configurations")) {
            
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                ServiceConfig config = new ServiceConfig(
                    rs.getString("service_type"),
                    rs.getInt("duration"),
                    rs.getDouble("base_price"),
                    rs.getString("required_resources").split(","),
                    rs.getString("required_staff").split(",")
                );
                serviceConfigs.put(config.getServiceType(), config);
            }
        } catch (SQLException e) {
            // Log error and use default configurations
            setDefaultConfigurations();
        }
    }

    private void setDefaultConfigurations() {
        serviceConfigs.put("CONSULTA", new ServiceConfig(
            "CONSULTA", 30, 150.0,
            new String[]{"CONSULTATION_ROOM"},
            new String[]{"VETERINARIAN"}
        ));
        serviceConfigs.put("VACINA", new ServiceConfig(
            "VACINA", 15, 80.0,
            new String[]{"CONSULTATION_ROOM"},
            new String[]{"VETERINARIAN", "TECHNICIAN"}
        ));
        serviceConfigs.put("BANHO", new ServiceConfig(
            "BANHO", 60, 70.0,
            new String[]{"GROOMING_STATION"},
            new String[]{"GROOMER"}
        ));
        serviceConfigs.put("TOSA", new ServiceConfig(
            "TOSA", 90, 100.0,
            new String[]{"GROOMING_STATION"},
            new String[]{"GROOMER"}
        ));
    }

    public BigDecimal calculatePrice(ServiceType serviceType, Map<String, Object> additionalFactors) {
        ServiceConfig config = serviceConfigs.get(serviceType.name());
        if (config == null) {
            throw new IllegalArgumentException("Service type not configured: " + serviceType);
        }

        double basePrice = config.getPrice();
        
        // Apply additional factors (size of pet, complexity, etc)
        if (additionalFactors != null) {
            if (additionalFactors.containsKey("size")) {
                String size = (String) additionalFactors.get("size");
                basePrice *= getSizeFactor(size);
            }
            if (additionalFactors.containsKey("complexity")) {
                Integer complexity = (Integer) additionalFactors.get("complexity");
                basePrice *= getComplexityFactor(complexity);
            }
        }

        return BigDecimal.valueOf(basePrice);
    }

    private double getSizeFactor(String size) {
        switch (size.toUpperCase()) {
            case "SMALL": return 1.0;
            case "MEDIUM": return 1.2;
            case "LARGE": return 1.5;
            case "GIANT": return 2.0;
            default: return 1.0;
        }
    }

    private double getComplexityFactor(int complexity) {
        switch (complexity) {
            case 1: return 1.0;  // Normal
            case 2: return 1.3;  // Complex
            case 3: return 1.5;  // Very complex
            default: return 1.0;
        }
    }

    public ServiceConfig getServiceConfig(ServiceType serviceType) {
        return serviceConfigs.get(serviceType.name());
    }

    public void updateServicePrice(String serviceType, double newPrice) throws SQLException {
        String sql = "UPDATE service_configurations SET base_price = ? WHERE service_type = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setDouble(1, newPrice);
            stmt.setString(2, serviceType);
            stmt.executeUpdate();
            
            // Update local cache
            ServiceConfig config = serviceConfigs.get(serviceType);
            if (config != null) {
                config.setPrice(newPrice);
            }
        }
    }
}