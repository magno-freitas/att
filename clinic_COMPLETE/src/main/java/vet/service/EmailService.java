package main.java.vet.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.*;

import main.java.vet.DatabaseConnection;

public class EmailService {
    private static final String FROM_EMAIL = "clinica@veterinaria.com";
// Import statements
// import com.amazonaws.services.secretsmanager.AWSSecretsManager;
// import com.amazonaws.services.secretsmanager.AWSSecretsManagerClientBuilder;
// import com.amazonaws.services.secretsmanager.model.GetSecretValueRequest;
// import com.amazonaws.services.secretsmanager.model.GetSecretValueResult;

private static final String EMAIL_PASSWORD = getSecretFromAWS("email_password_secret_name");

// Method to retrieve secret from AWS Secrets Manager
private static String getSecretFromAWS(String secretName) {
    AWSSecretsManager client = AWSSecretsManagerClientBuilder.defaultClient();
    GetSecretValueRequest getSecretValueRequest = new GetSecretValueRequest().withSecretId(secretName);
    GetSecretValueResult getSecretValueResult = client.getSecretValue(getSecretValueRequest);
    return getSecretValueResult.getSecretString();
}
    private Properties props;
    private Session session;

    public EmailService() {
        props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(FROM_EMAIL, EMAIL_PASSWORD);
            }
        });
    }

    public void sendEmail(String toEmail, String subject, String body) {
        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(FROM_EMAIL));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
            message.setSubject(subject);
            message.setText(body);

            // For development/testing, just print the message
            System.out.println("Sending email to: " + toEmail);
            System.out.println("Subject: " + subject);
            System.out.println("Message: " + body);

            // In production, uncomment this to actually send the email
            // Transport.send(message);

            // Log the email
            logEmailSent(toEmail, subject, body);

        } catch (MessagingException e) {
            throw new RuntimeException("Error sending email: " + e.getMessage(), e);
        }
    }

    private void logEmailSent(String toEmail, String subject, String message) {
        try {
            String query = "INSERT INTO email_log (to_email, subject, message, sent_at) VALUES (?, ?, ?, NOW())";
            try (Connection conn = DatabaseConnection.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(query)) {
                
                stmt.setString(1, toEmail);
                stmt.setString(2, subject);
                stmt.setString(3, message);
                
                stmt.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}