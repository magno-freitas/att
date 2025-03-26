package vet.service;

/**
 * Factory for creating service instances to ensure proper initialization
 * and dependency injection.
 */
public class ServiceFactory {
    private static final Object LOCK = new Object();
    private static volatile ServiceFactory instance;
    public static final String VERSION = "1.0.0";
    
    private final ClientService clientService;
    private final PetService petService;
    private final AppointmentService appointmentService;
    private final MedicalRecordService medicalRecordService;
    private final NotificationService notificationService;
    private final ReportingService reportingService;
    private final PriceService priceService;
    private final ResourceService resourceService;
    private final PaymentGatewayService paymentGatewayService;
    private final NotificationScheduler notificationScheduler;
    private final EmailService emailService;
    private final SMSService smsService;
    private final AuditService auditService;

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
            resourceService = new ResourceService();
            paymentGatewayService = new PaymentGatewayService(this);
            clientService = new ClientService();
            petService = new PetService();
            
            // Initialize medical and appointment services
            appointmentService = new AppointmentService(this);
            medicalRecordService = new MedicalRecordService();
            
            // Initialize supporting services
            reportingService = new ReportingService();
            notificationScheduler = new NotificationScheduler(notificationService);
        } catch (Exception e) {
            throw new ApplicationException("Failed to initialize services", e);
        }
    }

    public static ServiceFactory getInstance() {
        ServiceFactory result = instance;
        if (result == null) {
            synchronized (LOCK) {
                result = instance;
                if (result == null) {
                    instance = result = new ServiceFactory();
                }
            }
        }
        return result;
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

    public ResourceService getResourceService() {
        return resourceService;
    }
    
    public PaymentGatewayService getPaymentGatewayService() {
        return paymentGatewayService;
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
        try {
            // Shutdown services in reverse order
            notificationScheduler.shutdown();
            
            // Close database connections
            ConnectionPool.shutdown();
            LogManager.logInfo("Services shutdown completed");
        } catch (Exception e) {
            LogManager.logError("Error during service shutdown", e);
            throw new ApplicationException("Failed to shutdown services properly", e);
        }
    }
}