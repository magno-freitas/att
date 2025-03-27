package vet.dao;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import vet.config.DatabaseConfig;

import java.sql.Connection;
import java.sql.SQLException;

public class ConnectionPool {
    private static HikariDataSource dataSource;

    static {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(SecureConfig.getDecryptedProperty("db.url"));
        config.setUsername(SecureConfig.getDecryptedProperty("db.user"));
        config.setPassword(SecureConfig.getDecryptedProperty("db.password"));
        config.setDriverClassName(DatabaseConfig.getDriver());
        config.setMaximumPoolSize(DatabaseConfig.getPoolSize());
        config.setConnectionTimeout(DatabaseConfig.getPoolTimeout());

        // Additional settings for better performance
        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
        config.addDataSourceProperty("useServerPrepStmts", "true");

        dataSource = new HikariDataSource(config);
    }

    public static Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    public static void shutdown() {
        if (dataSource != null) {
            try {
                if (!dataSource.isClosed()) {
                    dataSource.close();
                }
            } catch (Exception e) {
                throw new ServiceException("Error shutting down connection pool", e);
            }
        }
    }
}