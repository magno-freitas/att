package main.java.vet.security;

import java.sql.*;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

import main.java.vet.*;
import main.java.vet.util.DatabaseConnection;

import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Base64;

public class SecurityService {
    private static final int ITERATIONS = 10000;
    private static final int KEY_LENGTH = 256;
    private static final String ALGORITHM = "PBKDF2WithHmacSHA1";

    public boolean hasPermission(String username, String permission) throws SQLException {
        String query = "SELECT r.permissions FROM users u " +
                      "JOIN roles r ON u.role = r.role_name " +
                      "WHERE u.username = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                String permissions = rs.getString("permissions");
                return permissions != null && permissions.contains(permission);
            }
        }
        return false;
    }

    public String hashPassword(String password) throws Exception {
        byte[] salt = generateSalt();
        byte[] hash = hashPassword(password.toCharArray(), salt);
        
        String saltStr = Base64.getEncoder().encodeToString(salt);
        String hashStr = Base64.getEncoder().encodeToString(hash);
        
        return String.format("%s:%s", saltStr, hashStr);
    }

    public boolean verifyPassword(String password, String storedHash) throws Exception {
        String[] parts = storedHash.split(":");
        byte[] salt = Base64.getDecoder().decode(parts[0]);
        byte[] hash = Base64.getDecoder().decode(parts[1]);
        
        byte[] testHash = hashPassword(password.toCharArray(), salt);
        return Arrays.equals(hash, testHash);
    }

    private byte[] generateSalt() {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);
        return salt;
    }

    private byte[] hashPassword(char[] password, byte[] salt) throws Exception {
        PBEKeySpec spec = new PBEKeySpec(password, salt, ITERATIONS, KEY_LENGTH);
        Arrays.fill(password, Character.MIN_VALUE);
        
        SecretKeyFactory skf = SecretKeyFactory.getInstance(ALGORITHM);
        return skf.generateSecret(spec).getEncoded();
    }
}