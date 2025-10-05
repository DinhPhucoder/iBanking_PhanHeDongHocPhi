// MyApplication.java
package com.example.ibanking_phanhedonghocphi;

import android.app.Application;

import java.math.BigInteger;

public class UserApp extends Application {
    private static UserApp instance;
    private BigInteger userID;
    private String username;
    private String fullName;
    private String email;
    private boolean isLoggedIn = false;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }

    public static UserApp getInstance() {
        if (instance == null) {
            instance = new UserApp();
        }
        return instance;
    }

    // Getter methods
    public BigInteger getUserID() {
        return userID;
    }

    public void setUserID(BigInteger userID) {
        this.userID= userID;
    }

    public String getFullName() {
        return fullName;
    }



}