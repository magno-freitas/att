package vet;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public class AppointmentReport {
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private Map<String, Integer> appointmentsByStatus;
    private List<Map<String, Object>> serviceStatistics;
    private Map<String, Double> revenueByService;

    public LocalDateTime getStartDate() { return startDate; }
    public void setStartDate(LocalDateTime startDate) { this.startDate = startDate; }

    public LocalDateTime getEndDate() { return endDate; }
    public void setEndDate(LocalDateTime endDate) { this.endDate = endDate; }

    public Map<String, Integer> getAppointmentsByStatus() { return appointmentsByStatus; }
    public void setAppointmentsByStatus(Map<String, Integer> appointmentsByStatus) { 
        this.appointmentsByStatus = appointmentsByStatus; 
    }

    public List<Map<String, Object>> getServiceStatistics() { return serviceStatistics; }
    public void setServiceStatistics(List<Map<String, Object>> serviceStatistics) { 
        this.serviceStatistics = serviceStatistics; 
    }

    public Map<String, Double> getRevenueByService() { return revenueByService; }
    public void setRevenueByService(Map<String, Double> revenueByService) { 
        this.revenueByService = revenueByService; 
    }
}