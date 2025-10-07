package com.example.ibanking_phanhedonghocphi.model;

import java.math.BigDecimal;
import java.math.BigInteger;

public class TransactionItem {
    private String transactionId;
    private BigDecimal amount;
    private String type;
    private String description;
    private String timestamp;

    // Constructor mặc định (cần thiết cho Retrofit)
    public TransactionItem() {
    }

    // Constructor cho dữ liệu fake
    public TransactionItem(long timestamp, BigDecimal amount, String description) {
        this.timestamp = String.valueOf(timestamp);
        this.amount = amount;
        this.description = description;
    }

    // Getters và Setters
    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}