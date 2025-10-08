package com.example.ibanking_phanhedonghocphi.model.dtoOtp;

public class GenerateOtpResponse {
    private String otpId;
    private String expiresAt;

    public GenerateOtpResponse() {
    }

    public GenerateOtpResponse(String otpId, String expiresAt) {
        this.otpId = otpId;
        this.expiresAt = expiresAt;
    }

    public String getOtpId() {
        return otpId;
    }

    public void setOtpId(String otpId) {
        this.otpId = otpId;
    }

    public String getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(String expiresAt) {
        this.expiresAt = expiresAt;
    }
}


