package com.java.yourfarmapp.Common;

public class Seller {

    private String user, phonenumber, email;

    public Seller() {
    }

    public Seller(String user, String phonenumber, String email) {
        this.user = user;
        this.phonenumber = phonenumber;
        this.email = email;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPhonenumber() {
        return phonenumber;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
