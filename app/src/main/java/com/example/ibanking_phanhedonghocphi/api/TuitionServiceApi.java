package com.example.ibanking_phanhedonghocphi.api;

import com.example.ibanking_phanhedonghocphi.model.Student;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface TuitionServiceApi {
    @GET("students/{id}")
    Call<Student> getStudentById(@Path("id") String studentId);
}
