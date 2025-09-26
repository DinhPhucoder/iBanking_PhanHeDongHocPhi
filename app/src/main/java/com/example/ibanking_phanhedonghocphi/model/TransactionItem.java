package com.example.ibanking_phanhedonghocphi.model;

public class TransactionItem {
    private String description;
    private double amount;
    private long timestamp;
    private int iconResId;

    public TransactionItem(long timestamp, double amount, String description) {
        this.timestamp = timestamp;
        this.amount = amount;
        this.description = description;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
