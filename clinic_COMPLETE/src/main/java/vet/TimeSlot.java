package main.java.vet;

import java.sql.Timestamp;

public class TimeSlot {
    private Timestamp startTime;
    private Timestamp endTime;
    private boolean available;
    
    public Timestamp getStartTime() {
        return startTime;
    }
    
    public void setStartTime(Timestamp startTime) {
        this.startTime = startTime;
    }
    
    public Timestamp getEndTime() {
        return endTime;
    }
    
    public void setEndTime(Timestamp endTime) {
        this.endTime = endTime;
    }
    
    public boolean isAvailable() {
        return available;
    }
    
    public void setAvailable(boolean available) {
        this.available = available;
    }
    
    @Override
    public String toString() {
        return String.format("%s - %s", 
            startTime.toString().substring(11, 16),
            endTime.toString().substring(11, 16));
    }
}