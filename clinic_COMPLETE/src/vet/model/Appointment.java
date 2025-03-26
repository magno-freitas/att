package vet.model;

import java.sql.Timestamp;
import java.time.LocalDateTime;

public class Appointment {
    private int appointmentId;
    private int clientId;
    private int petId;
    private ServiceType serviceType;
    private Timestamp startTime;
    private Timestamp endTime;
    private String status;
    private double price;
    private String notes;

    // Basic getters and setters
    public int getAppointmentId() { return appointmentId; }
    public void setAppointmentId(int appointmentId) { this.appointmentId = appointmentId; }
    
    public int getClientId() { return clientId; }
    public void setClientId(int clientId) { this.clientId = clientId; }
    
    public int getPetId() { return petId; }
    public void setPetId(int petId) { this.petId = petId; }
    
    public ServiceType getServiceType() { return serviceType; }
    public void setServiceType(ServiceType serviceType) { this.serviceType = serviceType; }
    
    public Timestamp getStartTime() { return startTime; }
    public void setStartTime(Timestamp startTime) { this.startTime = startTime; }
    
    public Timestamp getEndTime() { return endTime; }
    public void setEndTime(Timestamp endTime) { this.endTime = endTime; }
    
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    
    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }
    
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
    public String getService() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getService'");
    }
    public void setService(String string) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setService'");
    }
    public void setServiceType(ServiceType serviceType2) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setServiceType'");
    }
    public void setServiceType(ServiceType valueOf) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setServiceType'");
    }
}