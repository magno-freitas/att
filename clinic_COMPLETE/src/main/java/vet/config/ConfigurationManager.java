package main.java.vet.config;

import java.io.FileInputStream;
import java.util.Properties;
import java.util.logging.Logger;

public class ConfigurationManager {
    private static final Logger logger = Logger.getLogger(ConfigurationManager.class.getName());
    private static ConfigurationManager instance;
    private Properties properties;

    private ConfigurationManager() {
        properties = new Properties();
    }

    public static ConfigurationManager getInstance() {
        if (instance == null) {
            instance = new ConfigurationManager();
        }
        return instance;
    }

    public void initialize() {
        try {
            properties.load(new FileInputStream("database.properties"));
            logger.info("Configuration loaded successfully");
        } catch (Exception e) {
            logger.severe("Failed to load configuration: " + e.getMessage());
            throw new RuntimeException("Failed to load configuration", e);
        }
    }

    public String getProperty(String key) {
        return properties.getProperty(key);
    }
}