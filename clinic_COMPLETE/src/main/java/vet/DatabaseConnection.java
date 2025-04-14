package main.java.vet;

import java.sql.Connection;
import java.sql.SQLException;

import main.java.vet.dao.ConnectionPool;

public class DatabaseConnection {
    public static Connection getConnection() throws SQLException {
        return ConnectionPool.getConnection();
    }
    
    public static void closeConnection(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                // Log error if needed
            }
        }
    }
}