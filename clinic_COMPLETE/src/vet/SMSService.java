package vet;

public class SMSService {
    public void sendSMS(String phoneNumber, String message) {
        // In a real implementation, this would integrate with an SMS gateway service
        // For now, we'll just print the message
        System.out.println("Sending SMS to " + phoneNumber + ": " + message);
        
        // Log the SMS
        try {
            String query = "INSERT INTO sms_log (phone_number, message, sent_at) VALUES (?, ?, NOW())";
            try (Connection conn = DatabaseConnection.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(query)) {
                
                stmt.setString(1, phoneNumber);
                stmt.setString(2, message);
                
                stmt.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}