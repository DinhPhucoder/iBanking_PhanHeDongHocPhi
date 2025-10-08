package com.example.ibanking_phanhedonghocphi.model.dtoOtp;

import java.math.BigInteger;

public class EmailRequest {
    private BigInteger userId;
    private String type;
    private String otpId;
    private String transactionId;      // used when type = CONFIRMATION
    private java.math.BigDecimal amount; // used when type = CONFIRMATION
    private String mssv;               // used when type = CONFIRMATION

    public EmailRequest() {}

    public EmailRequest(BigInteger userId, String otpId) {
        this.userId = userId;
        this.type = "OTP";  // Mặc định là OTP
        this.otpId = otpId;
    }

    // Constructor cho email xác nhận giao dịch
    public EmailRequest(BigInteger userId, String transactionId, java.math.BigDecimal amount, String mssv) {
        this.userId = userId;
        this.type = "CONFIRMATION";
        this.transactionId = transactionId;
        this.amount = amount;
        this.mssv = mssv;
    }

    public BigInteger getUserId() {
        return userId;
    }

    public void setUserId(BigInteger userId) {
        this.userId = userId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getOtpId() {
        return otpId;
    }

    public void setOtpId(String otpId) {
        this.otpId = otpId;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public java.math.BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(java.math.BigDecimal amount) {
        this.amount = amount;
    }

    public String getMssv() {
        return mssv;
    }

    public void setMssv(String mssv) {
        this.mssv = mssv;
    }
}

