package com.example.ibanking_phanhedonghocphi.model.dtoOtp;

import java.math.BigInteger;

public class EmailRequest {
    private BigInteger userId;
    private String type;
    private String otpId;

    public EmailRequest() {}

    public EmailRequest(BigInteger userId, String otpId) {
        this.userId = userId;
        this.type = "OTP";  // Mặc định là OTP
        this.otpId = otpId;
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
}

