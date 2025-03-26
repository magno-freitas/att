package vet.service;

/**
 * Base class for all services to ensure proper initialization
 */
public abstract class BaseService {
    protected final ServiceFactory serviceFactory;
    
    protected BaseService(ServiceFactory serviceFactory) {
        if (serviceFactory == null) {
            throw new IllegalArgumentException("ServiceFactory cannot be null");
        }
        this.serviceFactory = serviceFactory;
    }
}