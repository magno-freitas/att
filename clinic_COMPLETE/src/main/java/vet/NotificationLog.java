package main.java.vet;

import java.sql.Timestamp;

public class NotificationLog {
    private int id;
    private int referenceId;
    private String type;
    private String message;
    private Timestamp sentAt;
    
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public int getReferenceId() {
        return referenceId;
    }
    
    public void setReferenceId(int referenceId) {
        this.referenceId = referenceId;
    }
    
    public String getType() {
        return type;
    }
    
    public void setType(String type) {
        this.type = type;
    }
    
    public String getMessage() {
        return message;
    }
    
    public void setMessage(String message) {
        this.message = message;
    }
    
    public Timestamp getSentAt() {
        return sentAt;
    }
    
    public void setSentAt(Timestamp sentAt) {
        this.sentAt = sentAt;
    }
}