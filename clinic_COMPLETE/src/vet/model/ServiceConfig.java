package vet.model;

public class ServiceConfig {
    private String serviceType;
    private int duration;
    private double price;
    private String[] requiredResources;
    private String[] requiredStaff;

    public ServiceConfig(String serviceType, int duration, double price, String[] requiredResources, String[] requiredStaff) {
        this.serviceType = serviceType;
        this.duration = duration;
        this.price = price;
        this.requiredResources = requiredResources;
        this.requiredStaff = requiredStaff;
    }

    public String getServiceType() { return serviceType; }
    public void setServiceType(String serviceType) { this.serviceType = serviceType; }

    public int getDuration() { return duration; }
    public void setDuration(int duration) { this.duration = duration; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public String[] getRequiredResources() { return requiredResources; }
    public void setRequiredResources(String[] requiredResources) { this.requiredResources = requiredResources; }

    public String[] getRequiredStaff() { return requiredStaff; }
    public void setRequiredStaff(String[] requiredStaff) { this.requiredStaff = requiredStaff; }
}