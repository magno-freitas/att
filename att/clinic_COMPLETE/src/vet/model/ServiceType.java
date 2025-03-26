package vet.model;

public enum ServiceType {
    CONSULTATION,
    VACCINATION,
    GROOMING,
    SURGERY,
    EMERGENCY,
    CHECKUP;

    private int defaultDurationMinutes;

    static {
        CONSULTATION.defaultDurationMinutes = 30;
        VACCINATION.defaultDurationMinutes = 15;
        GROOMING.defaultDurationMinutes = 60;
        SURGERY.defaultDurationMinutes = 120;
        EMERGENCY.defaultDurationMinutes = 60;
        CHECKUP.defaultDurationMinutes = 20;
    }

    public int getDefaultDurationMinutes() {
        return defaultDurationMinutes;
    }
}