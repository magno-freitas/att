package vet.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ApplicationConfig {
    private static final String CONFIG_FILE = "application.properties";
    private static Properties properties;
    
    public static synchronized void loadConfiguration() {
        if (properties != null) {
            return;
        }
        properties = new Properties();
        try {
            File configFile = new File(CONFIG_FILE);
            if (configFile.exists()) {
                properties.load(new FileInputStream(configFile));
            }
            setDefaults();
        } catch (IOException e) {
            setDefaults();
        }
    }
    
    private static void setDefaults() {
        properties.putIfAbsent("app.name", "Vet Clinic Management");
        properties.putIfAbsent("app.version", ServiceFactory.VERSION);
        properties.putIfAbsent("app.max.appointments", "3");
        properties.putIfAbsent("app.max.consultations", "2");
        properties.putIfAbsent("app.working.hours.start", "09:00");
        properties.putIfAbsent("app.working.hours.end", "18:00");
    }
    
    public static String getProperty(String key) {
        return properties.getProperty(key);
    }
    
    public static int getIntProperty(String key) {
        return Integer.parseInt(properties.getProperty(key));
    }
}