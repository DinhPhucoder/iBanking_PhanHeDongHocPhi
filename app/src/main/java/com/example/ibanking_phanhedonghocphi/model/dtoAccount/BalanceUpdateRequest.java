package com.example.ibanking_phanhedonghocphi.model.dtoAccount;

import java.math.BigDecimal;
import java.math.BigInteger;

public class BalanceUpdateRequest {
    private BigDecimal amount;
    private Long transactionId;
    private BigInteger userId;

    public BalanceUpdateRequest() {}

    public BalanceUpdateRequest(BigDecimal amount, Long transactionId, BigInteger userId) {
        this.amount = amount;
        this.transactionId = transactionId;
        this.userId = userId;
    }

    // Getters and Setters
    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }

    public Long getTransactionId() { return transactionId; }
    public void setTransactionId(Long transactionId) { this.transactionId = transactionId; }

    public BigInteger getUserId() { return userId; }
    public void setUserId(BigInteger userId) { this.userId = userId; }
}