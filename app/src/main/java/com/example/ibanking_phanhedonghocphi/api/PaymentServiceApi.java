package com.example.ibanking_phanhedonghocphi.api;

import com.example.ibanking_phanhedonghocphi.model.dtoPayment.*;

import java.math.BigInteger;
import retrofit2.Call;
import retrofit2.http.*;

public interface PaymentServiceApi {

    // POST initiate payment
    @POST("payments")
    Call<PaymentInitResponse> initiatePayment(@Body PaymentInitRequest request);

    // PUT confirm payment
    @PUT("payments/{transactionId}/confirm")
    Call<PaymentConfirmResponse> confirmPayment(@Path("transactionId") String transactionId, @Body PaymentConfirmRequest request);
}