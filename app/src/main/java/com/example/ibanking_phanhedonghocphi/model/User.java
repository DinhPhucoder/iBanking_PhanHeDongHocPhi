package com.example.ibanking_phanhedonghocphi.model;

public class User {
    private Long userId;
    private String username;
    private String fullName;
    private String email;
    private String phone;

    public User() {
        // Constructor mặc định
    }

    // Getters and setters
    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getFullName() {
        return fullName;
    }

}
