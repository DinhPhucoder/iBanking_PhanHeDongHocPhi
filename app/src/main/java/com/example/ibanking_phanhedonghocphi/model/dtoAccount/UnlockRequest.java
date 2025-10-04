package com.example.ibanking_phanhedonghocphi.model.dtoAccount;
import java.math.BigInteger;

public class UnlockRequest {
    private BigInteger userId;
    private String lockKey;

    public UnlockRequest() {}

    public UnlockRequest(BigInteger userId, String lockKey) {
        this.userId = userId;
        this.lockKey = lockKey;
    }

    // Getters and Setters
    public BigInteger getUserId() { return userId; }
    public void setUserId(BigInteger userId) { this.userId = userId; }

    public String getLockKey() { return lockKey; }
    public void setLockKey(String lockKey) { this.lockKey = lockKey; }
}
