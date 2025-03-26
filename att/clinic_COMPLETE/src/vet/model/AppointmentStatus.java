package vet.model;

public enum AppointmentStatus {
    SCHEDULED,
    IN_PROGRESS,
    COMPLETED,
    CANCELLED,
    RESCHEDULED;
    
    @Override
    public String toString() {
        return name();
    }
}