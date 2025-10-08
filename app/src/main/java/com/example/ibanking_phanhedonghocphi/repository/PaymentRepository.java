package com.example.ibanking_phanhedonghocphi.repository;

import com.example.ibanking_phanhedonghocphi.api.ApiClient;
import com.example.ibanking_phanhedonghocphi.api.PaymentServiceApi;
import com.example.ibanking_phanhedonghocphi.model.dtoPayment.PaymentConfirmRequest;
import com.example.ibanking_phanhedonghocphi.model.dtoPayment.PaymentConfirmResponse;
import com.example.ibanking_phanhedonghocphi.model.dtoPayment.PaymentInitRequest;
import com.example.ibanking_phanhedonghocphi.model.dtoPayment.PaymentInitResponse;
import com.example.ibanking_phanhedonghocphi.model.dtoPayment.TransactionIdResponse;

import retrofit2.Call;

public class PaymentRepository {
    private final PaymentServiceApi api;

    public PaymentRepository() {
        this.api = ApiClient.getPaymentApiService();
    }

    public Call<PaymentInitResponse> initiate(PaymentInitRequest request) {
        return api.initiatePayment(request);
    }

    public Call<PaymentConfirmResponse> confirm(PaymentConfirmRequest request) {
        return api.confirmPayment(request);
    }

    public Call<TransactionIdResponse> getTransactionId() {
        return api.getTransactionId();
    }

    public Call<PaymentInitResponse> getPending(java.math.BigInteger userId) {
        return api.getPending(userId);
    }
}


