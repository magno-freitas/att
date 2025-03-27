package vet.service;

import vet.SMSService;
import vet.dao.ConnectionPool;
import vet.exception.ApplicationException;
import vet.exception.ServiceException;
import java.sql.Connection;

/**
 * Factory for creating service instances with proper initialization order
 */
public class ServiceFactory {
    private static ServiceFactory instance;
    
    // Basic services
    private final EmailService emailService;
    private final SMSService smsService;
    private final AuditService auditService;
    
    // Core business services
    private final ClientService clientService;
    private final PetService petService;
    private final AppointmentService appointmentService;
    private final MedicalRecordService medicalRecordService;
    private final PriceService priceService;
    
    // Supporting services
    private final NotificationService notificationService;
    private final ReportingService reportingService;
    private final NotificationScheduler notificationScheduler;
    
    private ServiceFactory() {
        // Initialize services in dependency order
        emailService = new EmailService();
        smsService = new SMSService();
        auditService = new AuditService();
        
        notificationService = new NotificationService(emailService, smsService);
        notificationScheduler = new NotificationScheduler(notificationService);
        
        // Initialize business services
        clientService = new ClientService();
        petService = new PetService();
        priceService = new PriceService();
        reportingService = new ReportingService();
        
        // Initialize services that depend on others
        appointmentService = new AppointmentService(this);
        medicalRecordService = new MedicalRecordService(this);
        
        Runtime.getRuntime().addShutdownHook(new Thread(this::shutdownServices));
    }
    
    public static synchronized ServiceFactory getInstance() {
        if (instance == null) {
            instance = new ServiceFactory();
        }
        return instance;
    }
    
    // Service getters
    public EmailService getEmailService() { return emailService; }
    public SMSService getSMSService() { return smsService; }
    public AuditService getAuditService() { return auditService; }
    public ClientService getClientService() { return clientService; }
    public PetService getPetService() { return petService; }
    public AppointmentService getAppointmentService() { return appointmentService; }
    public MedicalRecordService getMedicalRecordService() { return medicalRecordService; }
    public NotificationService getNotificationService() { return notificationService; }
    public ReportingService getReportingService() { return reportingService; }
    public PriceService getPriceService() { return priceService; }
    public NotificationScheduler getNotificationScheduler() { return notificationScheduler; }
    public ResourceService getResourceService() { return new ResourceService(); }

    public void startServices() {
        try {
            // Test database connectivity
            try (Connection conn = ConnectionPool.getConnection()) {
                // Connection test successful
            }
            
            // Start background services
            notificationScheduler.start();
            
            // Log successful startup
            auditService.logAction("Services started successfully", "System");
        } catch (Exception e) {
            throw new ServiceException("Failed to start services", e);
        }
    }

    public void shutdownServices() {
        try {
            // Shutdown services in reverse order
            notificationScheduler.shutdown();
            
            // Shutdown all active services
            if (appointmentService != null) {
                auditService.logAction("Shutting down appointment service", "System");
            }
            if (medicalRecordService != null) {
                auditService.logAction("Shutting down medical record service", "System");
            }
            
            // Shutdown core services
            if (notificationService != null) {
                auditService.logAction("Shutting down notification service", "System");
            }
            
            // Shutdown infrastructure last
            ConnectionPool.shutdown();
            auditService.logAction("Services shut down successfully", "System");
        } catch (Exception e) {
            throw new ServiceException("Failed to shutdown services", e);
        }
    }
}