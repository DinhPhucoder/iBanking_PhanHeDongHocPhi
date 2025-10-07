package com.example.ibanking_phanhedonghocphi.model;

import android.app.Application;

import com.google.gson.annotations.SerializedName;

import java.math.BigInteger;

public class User extends Application {
    private static User instance;
    private BigInteger userId;
    private String username;
    @SerializedName("full_name")
    private String fullName;
    private String email;
    private String phone;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }
    public static User getInstance() {
        if (instance == null) {
            instance = new User();
        }
        return instance;
    }

    // Getters and setters
    public BigInteger getUserId() {
        return userId;
    }

    public void setUserId(BigInteger userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
