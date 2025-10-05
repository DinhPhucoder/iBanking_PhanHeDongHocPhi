package com.example.ibanking_phanhedonghocphi.api;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.math.BigInteger;
import java.math.BigDecimal;

public class ApiClient {
    // URL cho AccountService

    private static String USER_BASE_URL = "http://10.0.2.2:8081/";
    // Dien thoai Phu: 192.168.1.47
    private static String ACCOUNT_BASE_URL = "http://10.0.2.2:8082/";
    // URL cho PaymentService
    private static String PAYMENT_BASE_URL = "http://10.0.2.2:8083/";

    private static Retrofit accountRetrofit;
    private static Retrofit paymentRetrofit;
    private static Retrofit userRetrofit;


    // Custom Gson để handle BigInteger và BigDecimal
    private static Gson createGson() {
        return new GsonBuilder()
                .registerTypeAdapter(BigInteger.class, new TypeAdapter<BigInteger>() {
                    @Override
                    public void write(JsonWriter out, BigInteger value) throws IOException {
                        out.value(value.toString());
                    }

                    @Override
                    public BigInteger read(JsonReader in) throws IOException {
                        return new BigInteger(in.nextString());
                    }
                })
                .registerTypeAdapter(BigDecimal.class, new TypeAdapter<BigDecimal>() {
                    @Override
                    public void write(JsonWriter out, BigDecimal value) throws IOException {
                        out.value(value.toString());
                    }

                    @Override
                    public BigDecimal read(JsonReader in) throws IOException {
                        return new BigDecimal(in.nextString());
                    }
                })
                .create();
    }

    // Retrofit cho UserService
    public static Retrofit getUserRetrofit() {
        if (userRetrofit == null) {
            userRetrofit = new Retrofit.Builder()
                    .baseUrl(USER_BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return userRetrofit;
    }

    // Retrofit cho AccountService
    public static Retrofit getAccountRetrofit() {
        if (accountRetrofit == null) {
            accountRetrofit = new Retrofit.Builder()
                    .baseUrl(ACCOUNT_BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(createGson()))
                    .build();
        }
        return accountRetrofit;
    }

    // Retrofit cho PaymentService
    public static Retrofit getPaymentRetrofit() {
        if (paymentRetrofit == null) {
            paymentRetrofit = new Retrofit.Builder()
                    .baseUrl(PAYMENT_BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(createGson()))
                    .build();
        }
        return paymentRetrofit;
    }

    // API Service cho UserService
    public static ApiService getUserApiService() {
        return getUserRetrofit().create(ApiService.class);
    }

    // API Service cho AccountService
    public static AccountServiceApi getAccountApiService() {
        return getAccountRetrofit().create(AccountServiceApi.class);
    }

    // API Service cho PaymentService
    public static PaymentServiceApi getPaymentApiService() {
        return getPaymentRetrofit().create(PaymentServiceApi.class);
    }
}