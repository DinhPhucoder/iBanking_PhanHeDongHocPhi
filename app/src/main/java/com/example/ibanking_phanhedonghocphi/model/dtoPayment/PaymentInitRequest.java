package com.example.ibanking_phanhedonghocphi.model.dtoPayment;


import java.math.BigDecimal;
import java.math.BigInteger;

public class PaymentInitRequest {
    private BigInteger userId;
    private String mssv;
    private BigDecimal amount;

    public PaymentInitRequest() {}

    public PaymentInitRequest(BigInteger userId, String mssv, BigDecimal amount) {
        this.userId = userId;
        this.mssv = mssv;
        this.amount = amount;
    }

    // Getters and Setters
    public BigInteger getUserId() { return userId; }
    public void setUserId(BigInteger userId) { this.userId = userId; }

    public String getMssv() { return mssv; }
    public void setMssv(String mssv) { this.mssv = mssv; }

    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }
}
