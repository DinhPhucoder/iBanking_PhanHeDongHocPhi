package com.example.ibanking_phanhedonghocphi.model.dtoPayment;

public class PaymentConfirmRequest {
    private String transactionId;
    private String otpId;
    private String otpCode;

    public PaymentConfirmRequest() {}

    public PaymentConfirmRequest(String transactionId, String otpId, String otpCode) {
        this.transactionId = transactionId;
        this.otpId = otpId;
        this.otpCode = otpCode;
    }

    // Getters and Setters
    public String getTransactionId() { return transactionId; }
    public void setTransactionId(String transactionId) { this.transactionId = transactionId; }

    public String getOtpId() { return otpId; }
    public void setOtpId(String otpId) { this.otpId = otpId; }

    public String getOtpCode() { return otpCode; }
    public void setOtpCode(String otpCode) { this.otpCode = otpCode; }
}
