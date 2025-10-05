package com.example.ibanking_phanhedonghocphi.model.dtoPayment;

public class PaymentConfirmResponse {
    private String status;
    private String transactionId;

    public PaymentConfirmResponse() {}

    public PaymentConfirmResponse(String status, String transactionId) {
        this.status = status;
        this.transactionId = transactionId;
    }

    // Getters and Setters
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getTransactionId() { return transactionId; }
    public void setTransactionId(String transactionId) { this.transactionId = transactionId; }
}
