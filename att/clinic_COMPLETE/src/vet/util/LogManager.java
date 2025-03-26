package vet.util;

import java.util.logging.Level;
import java.util.logging.Logger;

public class LogManager {
    private static final Logger LOGGER = Logger.getLogger(LogManager.class.getName());
    
    public static void logError(String message, Throwable ex) {
        LOGGER.log(Level.SEVERE, message, ex);
    }
    
    public static void logInfo(String message) {
        LOGGER.log(Level.INFO, message);
    }
    
    public static void logWarning(String message) {
        LOGGER.log(Level.WARNING, message);
    }
    
    public static void logDebug(String message) {
        LOGGER.log(Level.FINE, message);
    }
}