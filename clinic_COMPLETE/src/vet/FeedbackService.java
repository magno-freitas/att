package vet;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;

public class FeedbackService {
    public void submitFeedback(int appointmentId, int rating, String comment) throws SQLException {
        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql = "INSERT INTO feedback (appointment_id, rating, comment, submission_date) VALUES (?, ?, ?, CURRENT_TIMESTAMP)";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, appointmentId);
                stmt.setInt(2, rating);
                stmt.setString(3, comment);
                stmt.executeUpdate();
            }
        }
    }

    public double getAverageRating() throws SQLException {
        // Implement average rating calculation
        return 0.0;
    }

    public void notifyLowRating(int appointmentId, int rating) {
        if (rating <= 2) {
            // Send notification to management about low rating
            EmailService.sendEmail("manager@vetclinic.com", "Low Rating Alert", 
                "Appointment " + appointmentId + " received a low rating of " + rating);
        }
    }
}