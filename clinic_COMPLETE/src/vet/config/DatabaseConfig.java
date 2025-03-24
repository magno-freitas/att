package vet.config;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class DatabaseConfig {
    private static final String CONFIG_FILE = "database.properties";
    private static Properties properties;

    static {
        properties = new Properties();
        try (FileInputStream input = new FileInputStream(CONFIG_FILE)) {
            properties.load(input);
        } catch (IOException e) {
            // If config file not found, use default values
            setDefaults();
        }
    }

    private static void setDefaults() {
        properties.setProperty("db.url", "jdbc:mysql://localhost:3306/vet_clinic");
        properties.setProperty("db.user", "root");
        properties.setProperty("db.password", "");
        properties.setProperty("db.driver", "com.mysql.cj.jdbc.Driver");
        properties.setProperty("db.pool.size", "10");
        properties.setProperty("db.pool.timeout", "30000");
    }

    public static String getUrl() {
        return properties.getProperty("db.url");
    }

    public static String getUser() {
        return properties.getProperty("db.user");
    }

    public static String getPassword() {
        return properties.getProperty("db.password");
    }

    public static String getDriver() {
        return properties.getProperty("db.driver");
    }

    public static int getPoolSize() {
        return Integer.parseInt(properties.getProperty("db.pool.size"));
    }

    public static int getPoolTimeout() {
        return Integer.parseInt(properties.getProperty("db.pool.timeout"));
    }
}