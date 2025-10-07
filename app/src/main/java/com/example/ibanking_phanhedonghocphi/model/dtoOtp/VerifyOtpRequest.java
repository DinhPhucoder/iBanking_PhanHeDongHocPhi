package com.example.ibanking_phanhedonghocphi.model.dtoOtp;

public class VerifyOtpRequest {
    private String otpId;
    private String otpCode;

    public VerifyOtpRequest(String otpId, String otpCode) {
        this.otpId = otpId;
        this.otpCode = otpCode;
    }

    public String getOtpId() {
        return otpId;
    }

    public void setOtpId(String otpId) {
        this.otpId = otpId;
    }

    public String getOtpCode() {
        return otpCode;
    }

    public void setOtpCode(String otpCode) {
        this.otpCode = otpCode;
    }
}


