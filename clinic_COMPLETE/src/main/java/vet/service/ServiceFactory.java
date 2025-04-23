package main.java.vet.service;

import main.java.vet.model.Client;
import main.java.vet.model.Pet;

public class ServiceFactory {
    private static ServiceFactory instance;
    
    private final ClientService clientService;
    private final PetService petService;
    private final AppointmentService appointmentService;
    private final NotificationService notificationService;
    private final PriceService priceService;
    private final ReportingService reportingService;
    private final ValidationService validationService;
    
    private ServiceFactory() {
        this.clientService = new ClientService();
        this.petService = new PetService();
        this.notificationService = new NotificationService();
        this.priceService = new PriceService();
        this.reportingService = new ReportingService();
        this.validationService = new ValidationService();
        this.appointmentService = new AppointmentService(this);
    }
    
    public static ServiceFactory getInstance() {
        if (instance == null) {
            instance = new ServiceFactory();
        }
        return instance;
    }
    
    public ClientService getClientService() { return clientService; }
    public PetService getPetService() { return petService; }
    public AppointmentService getAppointmentService() { return appointmentService; }
    public NotificationService getNotificationService() { return notificationService; }
    public PriceService getPriceService() { return priceService; }
    public ReportingService getReportingService() { return reportingService; }
    public ValidationService getValidationService() { return validationService; }
}