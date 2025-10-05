package com.example.ibanking_phanhedonghocphi.model;

import java.math.BigInteger;

public class LoginResponse {
    private BigInteger userID;
    private String fullName;

    public BigInteger getUserID() {
        return userID;
    }
    public String getFullName() {
        return fullName;
    }
}
