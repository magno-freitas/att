package vet.dao;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import vet.config.DatabaseConfig;
import vet.exception.DatabaseException;
import java.sql.Connection;
import java.sql.SQLException;

public class ConnectionPool {
    private static volatile HikariDataSource dataSource;
    private static final Object LOCK = new Object();

    private static void initializeDataSource() {
        if (dataSource != null) {
            return;
        }
        synchronized (LOCK) {
            if (dataSource != null) {
                return;
            }
            try {
                HikariConfig config = new HikariConfig();
                config.setJdbcUrl(DatabaseConfig.getUrl());
                config.setUsername(DatabaseConfig.getUser());
                config.setPassword(DatabaseConfig.getPassword());
                config.setDriverClassName(DatabaseConfig.getDriver());
                config.setMaximumPoolSize(DatabaseConfig.getPoolSize());
                config.setConnectionTimeout(DatabaseConfig.getPoolTimeout());

                // Additional settings for better performance
                config.addDataSourceProperty("cachePrepStmts", "true");
                config.addDataSourceProperty("prepStmtCacheSize", "250");
                config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
                config.addDataSourceProperty("useServerPrepStmts", "true");

                dataSource = new HikariDataSource(config);
            } catch (Exception e) {
                throw new DatabaseException("Failed to initialize connection pool", e);
            }
        }
    }

    public static Connection getConnection() throws DatabaseException {
        if (dataSource == null) {
            initializeDataSource();
        }
        try {
            return dataSource.getConnection();
        } catch (SQLException e) {
            throw new DatabaseException("Failed to get database connection", e);
        }
    }

    public static void shutdown() {
        synchronized (LOCK) {
            if (dataSource != null) {
                dataSource.close();
                dataSource = null;
            }
        }
    }
}