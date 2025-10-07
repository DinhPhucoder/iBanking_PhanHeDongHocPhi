package com.example.ibanking_phanhedonghocphi.api;

import com.example.ibanking_phanhedonghocphi.model.dtoOtp.EmailRequest;
import com.example.ibanking_phanhedonghocphi.model.dtoOtp.GenerateOtpResponse;
import com.example.ibanking_phanhedonghocphi.model.dtoOtp.GenerateOtpRequest;
import com.example.ibanking_phanhedonghocphi.model.dtoOtp.VerifyOtpRequest;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface OTPNotificationServiceApi {

    @POST("otp/notifications/email")
    Call<Void> sendEmail(@Body EmailRequest request);

    @POST("otp/verify")
    Call<Void> verifyOtp(@Body VerifyOtpRequest request);

    @POST("otp/generate")
    Call<GenerateOtpResponse> generateOtp(@Body GenerateOtpRequest request);
}


