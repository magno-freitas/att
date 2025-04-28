package vet.config;

public class DatabaseConfig {
    private static final ConfigurationManager config = ConfigurationManager.getInstance();

    public static String getUrl() {
        return config.getProperty("db.url");
    }

    public static String getUser() {
        return config.getProperty("db.user");
    }

    public static String getPassword() {
        return config.getProperty("db.password");
    }

    public static String getDriver() {
        return config.getProperty("db.driver");
    }

    public static int getPoolSize() {
        return Integer.parseInt(config.getProperty("db.pool.size"));
    }

    public static long getPoolTimeout() {
        return Long.parseLong(config.getProperty("db.pool.timeout"));
    }
}