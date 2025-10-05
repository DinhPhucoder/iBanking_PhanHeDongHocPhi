package com.example.ibanking_phanhedonghocphi.model.dtoPayment;

import java.math.BigInteger;

public class PaymentConfirmRequest {
    private BigInteger transactionId;
    private BigInteger otpId;
    private String otpCode;

    public PaymentConfirmRequest() {}

    public PaymentConfirmRequest(BigInteger transactionId, BigInteger otpId, String otpCode) {
        this.transactionId = transactionId;
        this.otpId = otpId;
        this.otpCode = otpCode;
    }

    // Getters and Setters
    public BigInteger getTransactionId() { return transactionId; }
    public void setTransactionId(BigInteger transactionId) { this.transactionId = transactionId; }

    public BigInteger getOtpId() { return otpId; }
    public void setOtpId(BigInteger otpId) { this.otpId = otpId; }

    public String getOtpCode() { return otpCode; }
    public void setOtpCode(String otpCode) { this.otpCode = otpCode; }
}
