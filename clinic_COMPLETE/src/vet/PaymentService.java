package vet;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class PaymentService {
    public void processPayment(int appointmentId, double amount, String paymentMethod) throws SQLException {
        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql = "INSERT INTO payments (appointment_id, amount, payment_method, status, payment_date) VALUES (?, ?, ?, 'COMPLETED', CURRENT_TIMESTAMP)";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, appointmentId);
                stmt.setDouble(2, amount);
                stmt.setString(3, paymentMethod);
                stmt.executeUpdate();
            }
        }
    }

    public boolean verifyPayment(int appointmentId) throws SQLException {
        // Implement payment verification logic
        return true;
    }

    public void refundPayment(int appointmentId) throws SQLException {
        // Implement refund logic
        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql = "UPDATE payments SET status = 'REFUNDED' WHERE appointment_id = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, appointmentId);
                stmt.executeUpdate();
            }
        }
    }
}