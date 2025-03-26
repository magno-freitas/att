package vet;

import java.util.logging.LogManager;

import vet.config.DatabaseConfig;
import vet.dao.ConnectionPool;
import vet.service.ServiceFactory;

public class Application {
    public static void main(String[] args) {
        try {
            // Load database configuration
            ConfigurationManager.initialize();
            
            // Initialize services and database
            ServiceFactory serviceFactory = ServiceFactory.getInstance();
            ServiceInitializer.initializeServices(serviceFactory);

            LogManager.logInfo("Application started successfully");
            
            // Show main menu
            MainMenu.showMainMenu();
        } catch (Exception e) {
            System.err.println("Application failed to start: " + e.getMessage());
            e.printStackTrace();
        } finally {
            // Ensure proper shutdown
            // Shutdown all services
            ServiceInitializer.shutdownServices(ServiceFactory.getInstance());
            // Close database connections
            ConnectionPool.shutdown();
        }
    }
}