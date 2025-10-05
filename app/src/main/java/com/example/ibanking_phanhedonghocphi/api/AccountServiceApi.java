package com.example.ibanking_phanhedonghocphi.api;

import com.example.ibanking_phanhedonghocphi.model.TransactionItem;
import com.example.ibanking_phanhedonghocphi.model.dtoAccount.*;


import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import retrofit2.Call;
import retrofit2.http.*;

public interface AccountServiceApi {

    // GET balance
    @GET("accounts/{userId}/balance")
    Call<BigDecimal> getBalance(@Path("userId") BigInteger userId);

    // GET history
    @GET("accounts/{userId}/history")
    Call<List<TransactionItem>> getHistory(@Path("userId") BigInteger userId);

    // POST check balance
    @POST("accounts/checkBalance")
    Call<Boolean> checkBalance(@Body TransactionRequest request);

    // PUT update balance
    @PUT("accounts/{userId}/balance")
    Call<BalanceResponse> updateBalance(@Path("userId") BigInteger userId, @Body BalanceUpdateRequest request);

    // POST lock account
    @POST("accounts/{userId}/lock")
    Call<LockResponse> lockAccount(@Path("userId") BigInteger userId);

    // POST unlock account
    @POST("accounts/{userId}/unlock")
    Call<UnlockResponse> unlockAccount(@Path("userId") BigInteger userId, @Body UnlockRequest request);
}