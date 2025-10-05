package com.example.ibanking_phanhedonghocphi.model.dtoAccount;

import java.math.BigDecimal;
import java.math.BigInteger;

public class BalanceResponse {
    private BigInteger userId;
    private BigDecimal newBalance;

    public BalanceResponse() {}

    public BalanceResponse(BigInteger userId, BigDecimal newBalance) {
        this.userId = userId;
        this.newBalance = newBalance;
    }

    // Getters and Setters
    public BigInteger getUserId() { return userId; }
    public void setUserId(BigInteger userId) { this.userId = userId; }

    public BigDecimal getNewBalance() { return newBalance; }
    public void setNewBalance(BigDecimal newBalance) { this.newBalance = newBalance; }
}