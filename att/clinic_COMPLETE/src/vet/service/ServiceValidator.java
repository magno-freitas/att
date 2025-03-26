package vet.service;

import vet.exception.ValidationException;
import vet.model.ServiceType;
import vet.config.ApplicationConfig;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class ServiceValidator {
    
    public static void validateBusinessHours(LocalDateTime dateTime) {
        LocalTime time = dateTime.toLocalTime();
        LocalTime start = LocalTime.parse(ApplicationConfig.getProperty("app.working.hours.start"));
        LocalTime end = LocalTime.parse(ApplicationConfig.getProperty("app.working.hours.end"));
        
        if (time.isBefore(start) || time.isAfter(end)) {
            throw new ValidationException("Appointment time must be within business hours: " + start + " - " + end);
        }
    }
    
    public static void validateServiceType(ServiceType serviceType) {
        if (serviceType == null) {
            throw new ValidationException("Service type cannot be null");
        }
    }
    
    public static void validateDuration(LocalDateTime start, LocalDateTime end) {
        if (start.isAfter(end) || start.equals(end)) {
            throw new ValidationException("Invalid appointment duration");
        }
    }
}