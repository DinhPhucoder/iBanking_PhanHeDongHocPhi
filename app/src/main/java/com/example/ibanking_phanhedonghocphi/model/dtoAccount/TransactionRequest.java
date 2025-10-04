package com.example.ibanking_phanhedonghocphi.model.dtoAccount;

import java.math.BigDecimal;
import java.math.BigInteger;

public class TransactionRequest {
    private BigInteger userId;
    private String mssv;
    private String type;
    private BigDecimal amount;
    private String description;
    private String transactionId;

    // Constructors, getters, setters
    public TransactionRequest() {}

    public TransactionRequest(BigInteger userId, String mssv, String type, BigDecimal amount, String description, String transactionId) {
        this.userId = userId;
        this.mssv = mssv;
        this.type = type;
        this.amount = amount;
        this.description = description;
        this.transactionId = transactionId;
    }

    // Getters and Setters
    public BigInteger getUserId() { return userId; }
    public void setUserId(BigInteger userId) { this.userId = userId; }

    public String getMssv() { return mssv; }
    public void setMssv(String mssv) { this.mssv = mssv; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getTransactionId() { return transactionId; }
    public void setTransactionId(String transactionId) { this.transactionId = transactionId; }
}