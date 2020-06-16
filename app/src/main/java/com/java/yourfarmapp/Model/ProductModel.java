package com.java.yourfarmapp.Model;

import java.util.List;

public class ProductModel {
    private String cropProductID;
    private String cropName;
    private String cropPrice;
    private String cropDescription;
    private String cropImage;
    private String cropQuantity;
    private String cropQuantityType;

    private String userName;
    private String userKey;
    private String userPhoneNumber;

    List<UserModel> users;

    public ProductModel() {
    }

    public ProductModel(String cropQuantityType) {
        this.cropQuantityType = cropQuantity;
    }

    public String getCropProductID(String key) {
        return cropProductID;
    }

    public void setCropProductID(String cropProductID) {
        this.cropProductID = cropProductID;
    }

    public String getCropName() {
        return cropName;
    }

    public void setCropName(String cropName) {
        this.cropName = cropName;
    }

    public String getCropPrice() {
        return cropPrice;
    }

    public void setCropPrice(String cropPrice) {
        this.cropPrice = cropPrice;
    }

    public String getCropQuantity() {
        return cropQuantity;
    }

    public void setCropQuantity(String cropQuantity) {
        this.cropQuantity = cropQuantity;
    }

    public String getCropQuantityType() {
        return cropQuantityType;
    }

    public void setCropQuantityType(String cropQuantityType) {
        this.cropQuantityType = cropQuantityType;
    }

    public String getCropDescription() {
        return cropDescription;
    }

    public void setCropDescription(String cropDescription) {
        this.cropDescription = cropDescription;
    }

    public String getCropImage() {
        return cropImage;
    }

    public void setCropImage(String cropImage) {
        this.cropImage = cropImage;
    }

    public String getCropProductID() {
        return cropProductID;
    }

    public String getUserName() {
        return userName;
    }

    public String getUserKey() {
        return userKey;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setUserKey(String userKey) {
        this.userKey = userKey;
    }

    public String getUserPhoneNumber() {
        return userPhoneNumber;
    }

    public void setUserPhoneNumber(String userPhoneNumber) {
        this.userPhoneNumber = userPhoneNumber;
    }

}
