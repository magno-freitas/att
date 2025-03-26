package vet.config;

import vet.exception.ApplicationException;
import vet.util.LogManager;

/**
 * Centralized configuration management
 */
public class ConfigurationManager {
    private static boolean initialized = false;
    private static final Object LOCK = new Object();
    
    public static void initialize() {
        synchronized (LOCK) {
            if (initialized) {
                return;
            }
            
            try {
                // Load all configurations in proper order
                DatabaseConfig.loadConfiguration();
                ApplicationConfig.loadConfiguration();
                
                initialized = true;
                LogManager.logInfo("Configuration loaded successfully");
            } catch (Exception e) {
                throw new ApplicationException("Failed to load configuration", e);
            }
        }
    }
    
    public static void reload() {
        synchronized (LOCK) {
            initialized = false;
            initialize();
        }
    }
}