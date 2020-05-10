package com.java.yourfarmapp.Model;

public class UserModel {
    private String UID, email, password, number, fullName, address;
    private boolean isFarmer, isDealer;

    public UserModel() {

    }

    public UserModel(String UID, String email, String password) {
        this.UID = UID;
        this.email = email;
        this.password = password;
    }

    public UserModel(String UID, String email, String password, String number, String fullName, String address, boolean isFarmer, boolean isDealer) {
        this.UID = UID;
        this.email = email;
        this.password = password;
        this.number = number;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
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
