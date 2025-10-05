package com.example.ibanking_phanhedonghocphi.api;

import com.example.ibanking_phanhedonghocphi.model.LoginRequest;
import com.example.ibanking_phanhedonghocphi.model.LoginResponse;
import com.example.ibanking_phanhedonghocphi.model.Student;
import com.example.ibanking_phanhedonghocphi.model.TokenResponse;
import com.example.ibanking_phanhedonghocphi.model.User;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ApiService {
    @POST("api/auth/login")
    Call<LoginResponse> login(@Body LoginRequest loginRequest);
    @GET("users/{id}")
    Call<User> getUserById(@Path("id") long id);
    @GET("students/{id}")
    Call<Student> getStudentById(@Path("id") String studentId);
}
