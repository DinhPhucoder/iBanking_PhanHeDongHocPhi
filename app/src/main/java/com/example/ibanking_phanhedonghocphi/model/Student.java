package com.example.ibanking_phanhedonghocphi.model;

public class Student {
    private String mssv;
    private String fullName;
    private double tuitionFee;
    private String status;

    public Student() {
        // Constructor mặc định
    }

    // Getters and setters
    public String getMSSV() {
        return mssv;
    }
    public void setMSSV(String mssv) { this.mssv = mssv; }

    public String getFullName() {
        return fullName;
    }
    public void setFullName(String mssv) { this.fullName = fullName; }

    public double getTuitionFee() {
        return tuitionFee;
    }
    public void setTuitionFee(double tuitionFee) { this.tuitionFee = tuitionFee; }

    public String getStatus() {
        return status;
    }
    public void setStatus(String status) { this.status= status; }
}
