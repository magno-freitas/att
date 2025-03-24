package vet.service;

/**
 * Factory for creating service instances to ensure proper initialization
 * and dependency injection.
 */
public class ServiceFactory {
    private static ServiceFactory instance;
    
    private final ClientService clientService;
    private final PetService petService;
    private final AppointmentService appointmentService;
    private final MedicalRecordService medicalRecordService;
    private final NotificationService notificationService;
    private final ReportingService reportingService;
    private final PriceService priceService;
    private final NotificationScheduler notificationScheduler;
    private final EmailService emailService;
    private final SMSService smsService;
    private final AuditService auditService;

    private ServiceFactory() {
        // Initialize services with dependencies
        emailService = new EmailService();
        smsService = new SMSService();
        auditService = new AuditService();
        clientService = new ClientService();
        petService = new PetService();
        appointmentService = new AppointmentService();
        medicalRecordService = new MedicalRecordService();
        notificationService = new NotificationService();
        reportingService = new ReportingService();
        priceService = new PriceService();
        notificationScheduler = new NotificationScheduler();
    }

    public static synchronized ServiceFactory getInstance() {
        if (instance == null) {
            instance = new ServiceFactory();
        }
        return instance;
    }

    public ClientService getClientService() {
        return clientService;
    }

    public PetService getPetService() {
        return petService;
    }

    public AppointmentService getAppointmentService() {
        return appointmentService;
    }

    public MedicalRecordService getMedicalRecordService() {
        return medicalRecordService;
    }

    public NotificationService getNotificationService() {
        return notificationService;
    }

    public ReportingService getReportingService() {
        return reportingService;
    }

    public PriceService getPriceService() {
        return priceService;
    }

    public NotificationScheduler getNotificationScheduler() {
        return notificationScheduler;
    }

    public EmailService getEmailService() {
        return emailService;
    }

    public SMSService getSMSService() {
        return smsService;
    }

    public AuditService getAuditService() {
        return auditService;
    }

    public void startServices() {
        // Start background services
        notificationScheduler.start();
    }

    public void shutdownServices() {
        // Shutdown services gracefully
        notificationScheduler.shutdown();
    }
}