package com.example.ibanking_phanhedonghocphi.model.dtoAccount;

public class UnlockResponse {
    private Boolean unlocked;

    public UnlockResponse() {}

    public UnlockResponse(Boolean unlocked) {
        this.unlocked = unlocked;
    }

    // Getters and Setters
    public Boolean getUnlocked() { return unlocked; }
    public void setUnlocked(Boolean unlocked) { this.unlocked = unlocked; }
}