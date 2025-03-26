package vet.service;

import vet.model.PaymentRequest;
import vet.exception.PaymentException;

public class PaymentGatewayService extends BaseService {
    public PaymentGatewayService(ServiceFactory serviceFactory) {
        super(serviceFactory);
    }
    
    public void processPayment(PaymentRequest request) {
        // TODO: Implement actual payment processing
        // For now just simulate success
        System.out.println("Processing payment: " + request.getAmount() + " for " + request.getType());
    }
}