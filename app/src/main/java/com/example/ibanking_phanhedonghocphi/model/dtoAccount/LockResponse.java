package com.example.ibanking_phanhedonghocphi.model.dtoAccount;

import java.math.BigInteger;

public class LockResponse {
    private Boolean locked;
    private String lockKey;
    private BigInteger expiry;

    public LockResponse() {}

    public LockResponse(Boolean locked, String lockKey, BigInteger expiry) {
        this.locked = locked;
        this.lockKey = lockKey;
        this.expiry = expiry;
    }

    // Getters and Setters
    public Boolean getLocked() { return locked; }
    public void setLocked(Boolean locked) { this.locked = locked; }

    public String getLockKey() { return lockKey; }
    public void setLockKey(String lockKey) { this.lockKey = lockKey; }

    public BigInteger getExpiry() { return expiry; }
    public void setExpiry(BigInteger expiry) { this.expiry = expiry; }
}