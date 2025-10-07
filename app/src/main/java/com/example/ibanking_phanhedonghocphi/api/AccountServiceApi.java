package com.example.ibanking_phanhedonghocphi.api;

import com.example.ibanking_phanhedonghocphi.model.TransactionItem;
import com.example.ibanking_phanhedonghocphi.model.dtoAccount.*;


import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import retrofit2.Call;
import retrofit2.http.*;

public interface AccountServiceApi {

    @GET("accounts/{userId}/balance")
    Call<BigDecimal> getBalance(@Path("userId") BigInteger userId);

    @GET("accounts/{userId}/history")
    Call<List<TransactionItem>> getHistory(@Path("userId") BigInteger userId);

    // Tạo giao dịch (server tự sinh transactionId)
    @POST("transactions")
    Call<TransactionResponse> createTransaction(@Body TransactionRequest request);

    @PUT("accounts/{userId}/balance")
    Call<BalanceResponse> updateBalance(@Path("userId") BigInteger userId,
                                        @Body BalanceUpdateRequest request); // optional: nội bộ

    @POST("accounts/{userId}/lock")
    Call<LockResponse> lockAccount(@Path("userId") BigInteger userId);

    @POST("accounts/{userId}/unlock")
    Call<UnlockResponse> unlockAccount(@Path("userId") BigInteger userId,
                                       @Body UnlockRequest request);
}