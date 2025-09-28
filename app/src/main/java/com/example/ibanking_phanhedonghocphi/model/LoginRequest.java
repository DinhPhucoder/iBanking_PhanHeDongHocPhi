package com.example.ibanking_phanhedonghocphi.model;

public class LoginRequest {
    private String username;
    private String password;

    public LoginRequest() {
        // Constructor mặc định
    }

    public LoginRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }

    // Getters and setters
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
