package vet.payment;

import vet.*;
import vet.util.DatabaseConnection;

import java.math.BigDecimal;
import java.sql.*;
import java.util.UUID;

public class PaymentGatewayService {
    private final String API_KEY = "your-api-key";
    private final String API_SECRET = "your-api-secret";
    private final String GATEWAY_URL = "https://api.payment-gateway.com";

    public PaymentResult processPayment(PaymentRequest request) throws PaymentException {
        try {
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
            throw new PaymentException("Failed to process payment: " + e.getMessage());
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