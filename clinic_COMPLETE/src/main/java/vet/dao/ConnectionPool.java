package main.java.vet.dao;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import main.java.vet.config.DatabaseConfig;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Logger;

public class ConnectionPool {
    private static HikariDataSource dataSource;
    private static final Logger logger = Logger.getLogger(ConnectionPool.class.getName());

    public static void initialize() {
        try {
            HikariConfig config = new HikariConfig();
            config.setJdbcUrl(DatabaseConfig.getUrl());
            config.setUsername(DatabaseConfig.getUser());
            config.setPassword(DatabaseConfig.getPassword());
            config.setMaximumPoolSize(DatabaseConfig.getPoolSize());
            config.setConnectionTimeout(DatabaseConfig.getPoolTimeout());
            
            dataSource = new HikariDataSource(config);
            
            // Validate connection pool
            try (Connection conn = dataSource.getConnection()) {
                logger.info("Connection pool initialized successfully");
            }
        } catch (Exception e) {
            logger.severe("Failed to initialize connection pool: " + e.getMessage());
            throw new RuntimeException("Failed to initialize connection pool", e);
        }
    }

    public static Connection getConnection() throws SQLException {
        if (dataSource == null) {
            throw new SQLException("Connection pool is not initialized");
        }
        return dataSource.getConnection();
    }

    public static void shutdown() {
        if (dataSource != null) {
            dataSource.close();
            logger.info("Connection pool shut down successfully");
        }
    }
}
