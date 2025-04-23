package main.java.vet.payment;

import java.math.BigDecimal;
import java.sql.*;
import java.util.UUID;
import java.util.logging.Logger;

import main.java.vet.*;
import main.java.vet.util.DatabaseConnection;
import main.java.vet.util.ConfigurationManager;

public class PaymentGatewayService {
    private static final Logger logger = Logger.getLogger(PaymentGatewayService.class.getName());
    private final ConfigurationManager configManager;

    public PaymentGatewayService(ConfigurationManager configManager) {
        this.configManager = configManager;
    }

    public PaymentResult processPayment(PaymentRequest request) throws PaymentException {
        try {
            String apiKey = configManager.getSecureProperty("payment.gateway.apiKey");
            // Simulate payment gateway API call
            String transactionId = UUID.randomUUID().toString();
            
            // Save payment record
            savePaymentRecord(request, transactionId);
            
            return new PaymentResult(
                transactionId,
                "SUCCESS",
                "Payment processed successfully"
            );
        } catch (Exception e) {
            logger.severe("Payment processing failed: " + e.getMessage());
            throw new PaymentException("Failed to process payment", e); 
        }
    }

    public PaymentResult refundPayment(String transactionId, BigDecimal amount) throws PaymentException {
        try {
            // Simulate refund API call
            String refundId = UUID.randomUUID().toString();
            
            // Update payment record
            updatePaymentRecord(transactionId, "REFUNDED");
            
            return new PaymentResult(
                refundId,
                "REFUNDED",
                "Refund processed successfully"
            );
        } catch (Exception e) {
            throw new PaymentException("Failed to process refund: " + e.getMessage());
        }
    }

    private void savePaymentRecord(PaymentRequest request, String transactionId) throws SQLException {
        String sql = "INSERT INTO payments (appointment_id, transaction_id, amount, payment_method, status) " +
                    "VALUES (?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, request.getAppointmentId());
            stmt.setString(2, transactionId);
            stmt.setBigDecimal(3, request.getAmount());
            stmt.setString(4, request.getPaymentMethod());
            stmt.setString(5, "COMPLETED");
            stmt.executeUpdate();
        }
    }

    private void updatePaymentRecord(String transactionId, String status) throws SQLException {
        String sql = "UPDATE payments SET status = ? WHERE transaction_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, status);
            stmt.setString(2, transactionId);
            stmt.executeUpdate();
        }
    }
}