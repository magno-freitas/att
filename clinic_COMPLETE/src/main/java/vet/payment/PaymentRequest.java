package main.java.vet.payment;

import java.math.BigDecimal;

public class PaymentRequest {
    private int appointmentId;
    private BigDecimal amount;
    private String paymentMethod;
    private String currency;
    private String description;

    public PaymentRequest(int appointmentId, BigDecimal amount, String paymentMethod) {
        this.appointmentId = appointmentId;
        this.amount = amount;
        this.paymentMethod = paymentMethod;
        this.currency = "BRL";
    }

    public int getAppointmentId() { return appointmentId; }
    public void setAppointmentId(int appointmentId) { this.appointmentId = appointmentId; }

    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }

    public String getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }

    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}