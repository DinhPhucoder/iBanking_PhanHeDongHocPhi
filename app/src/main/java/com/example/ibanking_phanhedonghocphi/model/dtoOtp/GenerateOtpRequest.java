package com.example.ibanking_phanhedonghocphi.model.dtoOtp;

import java.math.BigInteger;

public class GenerateOtpRequest {
    private String transactionId;
    private BigInteger userId;

    public GenerateOtpRequest(String transactionId, BigInteger userId) {
        this.transactionId = transactionId;
        this.userId = userId;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public BigInteger getUserId() {
        return userId;
    }

    public void setUserId(BigInteger userId) {
        this.userId = userId;
    }
}


