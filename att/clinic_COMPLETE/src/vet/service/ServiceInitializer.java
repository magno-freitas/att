package vet.service;

import vet.dao.ConnectionPool;
import vet.exception.ApplicationException;
import vet.util.LogManager;
import java.sql.Connection;

public class ServiceInitializer {
    public static void initializeServices(ServiceFactory serviceFactory) {
        try {
            // Test database connection first
            testDatabaseConnection();
            
            // Initialize and start background services
            serviceFactory.getNotificationScheduler().start();
            
            LogManager.logInfo("Services initialized successfully");
        } catch (Exception e) {
            throw new ApplicationException("Failed to initialize services", e);
        }
    }
    
    private static void testDatabaseConnection() {
        try (Connection conn = ConnectionPool.getConnection()) {
            LogManager.logInfo("Database connection test successful");
        } catch (Exception e) {
            throw new ApplicationException("Database connection test failed", e);
        }
    }
    
    public static void shutdownServices(ServiceFactory serviceFactory) {
        try {
            // Shutdown services in reverse order
            serviceFactory.getNotificationScheduler().shutdown();
            
            // Close database connections
            ConnectionPool.shutdown();
            
            LogManager.logInfo("Services shutdown completed successfully");
        } catch (Exception e) {
            LogManager.logError("Error during service shutdown", e);
        }
    }
}