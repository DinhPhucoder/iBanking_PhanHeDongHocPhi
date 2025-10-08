package com.example.ibanking_phanhedonghocphi.api;

import com.example.ibanking_phanhedonghocphi.model.dtoPayment.*;

import java.math.BigInteger;
import retrofit2.Call;
import retrofit2.http.*;

public interface PaymentServiceApi {

    // POST initiate payment
    @POST("payments")
    Call<PaymentInitResponse> initiatePayment(@Body PaymentInitRequest request);

    // GET pre-generated transactionId (optional)
    @GET("payments/transaction-id")
    Call<TransactionIdResponse> getTransactionId();

    // POST confirm payment (backend yêu cầu body chứa transactionId, otpId, otpCode)
    @POST("payments/confirm")
    Call<PaymentConfirmResponse> confirmPayment(@Body PaymentConfirmRequest request);

    // GET pending payment for user
    @GET("payments/pending/{userId}")
    Call<PaymentInitResponse> getPending(@Path("userId") BigInteger userId);
}