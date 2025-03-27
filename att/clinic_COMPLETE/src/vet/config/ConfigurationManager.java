package vet.config;

import java.util.Properties;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.io.IOException;

public class ConfigurationManager {
    private static final String CONFIG_DIR = "config";
    private static ConfigurationManager instance;
    private final Properties properties;

    private ConfigurationManager() {
        properties = new Properties();
        loadProperties();
    }

    public static synchronized ConfigurationManager getInstance() {
        if (instance == null) {
            instance = new ConfigurationManager();
        }
        return instance;
    }

    private void loadProperties() {
        try {
            // Load non-sensitive properties
            if (Files.exists(Paths.get(CONFIG_DIR, "database.properties"))) {
                properties.load(Files.newInputStream(Paths.get(CONFIG_DIR, "database.properties")));
            }
            
            // Load encrypted properties
            if (Files.exists(Paths.get(CONFIG_DIR, "secure.properties"))) {
                Properties secureProps = new Properties();
                secureProps.load(Files.newInputStream(Paths.get(CONFIG_DIR, "secure.properties")));
                for (String key : secureProps.stringPropertyNames()) {
                    properties.setProperty(key, SecureConfig.getDecryptedProperty(key));
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to load configuration", e);
        }
    }

    public String getProperty(String key) {
        return properties.getProperty(key);
    }

    public String getProperty(String key, String defaultValue) {
        return properties.getProperty(key, defaultValue);
    }

    public int getIntProperty(String key, int defaultValue) {
        String value = properties.getProperty(key);
        return value != null ? Integer.parseInt(value) : defaultValue;
    }
}