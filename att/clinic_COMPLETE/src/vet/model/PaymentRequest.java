package vet.model;

import java.math.BigDecimal;

public class PaymentRequest {
    private final int referenceId;
    private final BigDecimal amount;
    private final String type;
    
    public PaymentRequest(int referenceId, BigDecimal amount, String type) {
        this.referenceId = referenceId;
        this.amount = amount;
        this.type = type;
    }
    
    public int getReferenceId() { return referenceId; }
    public BigDecimal getAmount() { return amount; }
    public String getType() { return type; }
}