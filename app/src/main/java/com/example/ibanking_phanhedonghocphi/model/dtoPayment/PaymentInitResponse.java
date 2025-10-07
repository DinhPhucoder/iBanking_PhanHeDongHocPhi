package com.example.ibanking_phanhedonghocphi.model.dtoPayment;

import java.math.BigInteger;

public class PaymentInitResponse {
    private String transactionId;
    private BigInteger otpId;

    public PaymentInitResponse() {}

    public PaymentInitResponse(String transactionId, BigInteger otpId) {
        this.transactionId = transactionId;
        this.otpId = otpId;
    }

    // Getters and Setters
    public String getTransactionId() { return transactionId; }
    public void setTransactionId(String transactionId) { this.transactionId = transactionId; }

    public BigInteger getOtpId() { return otpId; }
    public void setOtpId(BigInteger otpId) { this.otpId = otpId; }
}
