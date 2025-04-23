package main.java.vet.model;

public enum ServiceType {
    CONSULTA(30),      // 30 minutes duration
    BANHO(60),         // 60 minutes duration
    TOSA(90),         // 90 minutes duration
    VACINA(15);       // 15 minutes duration
    
    private final int defaultDurationMinutes;
    
    ServiceType(int defaultDurationMinutes) {
        this.defaultDurationMinutes = defaultDurationMinutes;
    }
    
    public int getDefaultDurationMinutes() {
        return defaultDurationMinutes;
    }
}