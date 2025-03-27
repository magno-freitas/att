package vet.service;

import vet.dao.ConnectionPool;
import vet.exception.ServiceException;
import java.sql.Connection;

public class ServiceInitializer {
    private static final ServiceInitializer instance = new ServiceInitializer();
    private final ServiceFactory serviceFactory;
    private boolean initialized = false;

    private ServiceInitializer() {
        this.serviceFactory = ServiceFactory.getInstance();
    }

    public static ServiceInitializer getInstance() {
        return instance;
    }

    public synchronized void initialize() {
        if (initialized) {
            return;
        }

        try {
            // Test database connectivity
            try (Connection conn = ConnectionPool.getConnection()) {
                // Connection test successful
            }

            // Start all services
            serviceFactory.startServices();
            initialized = true;
        } catch (Exception e) {
            throw new ServiceException("Failed to initialize services", e);
        }
    }

    public synchronized void shutdown() {
        if (!initialized) {
            return;
        }

        try {
            serviceFactory.shutdownServices();
            initialized = false;
        } catch (Exception e) {
            throw new ServiceException("Failed to shutdown services", e);
        }
    }

    public boolean isInitialized() {
        return initialized;
    }
}