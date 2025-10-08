package com.example.ibanking_phanhedonghocphi.model.dtoPayment;

public class PaymentInitResponse {
    private String transactionId;
    private String otpId;

    public PaymentInitResponse() {}

    public PaymentInitResponse(String transactionId, String otpId) {
        this.transactionId = transactionId;
        this.otpId = otpId;
    }

    // Getters and Setters
    public String getTransactionId() { return transactionId; }
    public void setTransactionId(String transactionId) { this.transactionId = transactionId; }

    public String getOtpId() { return otpId; }
    public void setOtpId(String otpId) { this.otpId = otpId; }
}
