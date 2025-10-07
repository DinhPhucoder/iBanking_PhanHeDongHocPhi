package com.example.ibanking_phanhedonghocphi.model.dtoPayment;

public class TransactionIdResponse {
    private String transactionId;

    public TransactionIdResponse() {}

    public TransactionIdResponse(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getTransactionId() { return transactionId; }
    public void setTransactionId(String transactionId) { this.transactionId = transactionId; }
}


