package vet.model;

public class Client {
    private int clientId;
    private String name;
    private String email;
    private String phone;
    private String address;
    
    public Client() {}
    
    public Client(int clientId, String name, String email, String phone, String address) {
        this.clientId = clientId;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.address = address;
    }
    
    // Getters and setters
    public int getClientId() { return clientId; }
    public void setClientId(int clientId) { this.clientId = clientId; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
}