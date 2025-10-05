package com.example.ibanking_phanhedonghocphi.model.dtoPayment;

import java.math.BigInteger;

public class PaymentInitResponse {
    private BigInteger transactionId;
    private BigInteger otpId;

    public PaymentInitResponse() {}

    public PaymentInitResponse(BigInteger transactionId, BigInteger otpId) {
        this.transactionId = transactionId;
        this.otpId = otpId;
    }

    // Getters and Setters
    public BigInteger getTransactionId() { return transactionId; }
    public void setTransactionId(BigInteger transactionId) { this.transactionId = transactionId; }

    public BigInteger getOtpId() { return otpId; }
    public void setOtpId(BigInteger otpId) { this.otpId = otpId; }
}
