package vet.service;

import vet.MedicalRecordService;
import vet.NotificationService;
import vet.ReportingService;
import vet.SMSService;
import vet.dao.ConnectionPool;
import vet.exception.ApplicationException;

/**
 * Factory for creating service instances with proper initialization order
 * and dependency injection.
 * @param <ClientService>
 * @param <PetService>
 */
public class ServiceFactory<ClientService, PetService> {
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
        try {
            // Initialize basic services first
            emailService = new EmailService();
            smsService = new SMSService();
            auditService = new AuditService();
            
            // Initialize notification system
            notificationService = new NotificationService(emailService, smsService);
            
            // Initialize core business services
            priceService = new PriceService();
            clientService = new ClientService();
            petService = new PetService();
            
            // Initialize medical and appointment services
            appointmentService = new AppointmentService();
            medicalRecordService = new MedicalRecordService();
            
            // Initialize supporting services
            reportingService = new ReportingService();
            notificationScheduler = new NotificationScheduler(notificationService);
        } catch (Exception e) {
            throw new ApplicationException("Failed to initialize services", e);
        }
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

    public void startServices() {
        try {
            // Ensure database connection is ready
            ConnectionPool.getConnection().close();
            
            // Start background services
            notificationScheduler.start();
        } catch (Exception e) {
            throw new ApplicationException("Failed to start services", e);
        }
    }

    public void shutdownServices() {
        try {
            // Shutdown services in reverse order
            notificationScheduler.shutdown();
        } catch (Exception e) {
            System.err.println("Error during service shutdown: " + e.getMessage());
        }
    }
}