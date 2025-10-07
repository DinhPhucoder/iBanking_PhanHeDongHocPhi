package com.example.ibanking_phanhedonghocphi.model.dtoOtp;

import java.time.OffsetDateTime;

public class GenerateOtpResponse {
    private String otpId;
    private OffsetDateTime expiresAt;

    public GenerateOtpResponse(String otpId, OffsetDateTime expiresAt) {
        this.otpId = otpId;
        this.expiresAt = expiresAt;
    }

    public String getOtpId() {
        return otpId;
    }

    public void setOtpId(String otpId) {
        this.otpId = otpId;
    }

    public OffsetDateTime getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(OffsetDateTime expiresAt) {
        this.expiresAt = expiresAt;
    }
}


