package com.java.yourfarmapp.Model;

public class UserModel {
    private String UID, email, password, phoneNumber, fullName, contactNumber, address;
    private boolean isFarmer, isDealer;

    public UserModel() {

    }

    public UserModel(String UID, String email, String password) {
        this.UID = UID;
        this.email = email;
        this.password = password;
    }

    public UserModel(String email, String password, String phoneNumber, String fullName, String address, boolean isFarmer, boolean isDealer) {
        this.email = email;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.fullName = fullName;
        this.address = address;
        this.isFarmer = isFarmer;
        this.isDealer = isDealer;
    }

    public String getUID() {
        return UID;
    }

    public void setUID(String UID) {
        this.UID = UID;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public boolean isFarmer() {
        return isFarmer;
    }

    public void setFarmer(boolean farmer) {
        isFarmer = farmer;
    }

    public boolean isDealer() {
        return isDealer;
    }

    public void setDealer(boolean dealer) {
        isDealer = dealer;
    }
}
