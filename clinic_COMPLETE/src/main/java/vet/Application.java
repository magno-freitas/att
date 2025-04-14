package main.java.vet;

import java.util.logging.Logger;

import main.java.vet.config.ConfigurationManager;
import main.java.vet.dao.ConnectionPool;
import main.java.vet.service.ServiceFactory;

public class Application {
    private static final Logger logger = Logger.getLogger(Application.class.getName());

    public static void main(String[] args, ServiceFactory ConfigurationManager) {
            try {
                // Initialize configuration
                ConfigurationManager.getInstance().initialize();
            
            // Initialize database connection pool
            ConnectionPool.initialize();
            
            // Initialize services
            ServiceFactory.getInstance().initialize();

            logger.info("Application started successfully");
            
            // Show main menu
            MainMenu.show();
        } catch (Exception e) {
            logger.severe("Application failed to start: " + e.getMessage());
            e.printStackTrace();
        } finally {
            shutdown();
        }
    }

    private static void shutdown() {
        try {
            ServiceFactory.getInstance().shutdown();
            ConnectionPool.closePool();
            logger.info("Application shutdown completed");
        } catch (Exception e) {
            logger.severe("Error during shutdown: " + e.getMessage());
        }
    }
}