package com.example.fuelisticv2seller.Model;

public class SellerUserModel {

    private String fullName, phoneNo , aadhaar , email, username, password;
    private boolean active ;

    public SellerUserModel() {
    }

    public SellerUserModel(String fullName, String phoneNo, String aadhaar, String email, String username, String password, boolean active) {
        this.fullName = fullName;
        this.phoneNo = phoneNo;
        this.aadhaar = aadhaar;
        this.email = email;
        this.username = username;
        this.password = password;
        this.active = active;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

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

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getAadhaar() {
        return aadhaar;
    }

    public void setAadhaar(String aadhaar) {
        this.aadhaar = aadhaar;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
