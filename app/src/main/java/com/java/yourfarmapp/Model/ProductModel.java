package com.java.yourfarmapp.Model;

import java.util.List;

public class ProductModel {
    private String cropProductID;
    private String cropName;
    private String cropDescription;
    private String cropPrice;
    private String cropImage;
    private String cropQuantity;
    private String cropQuantityType;

    private String userKey;
    private String fullName;
    private String number;

    private String farmerProfilePic;

    List<UserModel> users;

    public ProductModel() {
    }

    public ProductModel(String cropProductID, String cropName, String cropDescription) {
        this.cropProductID = cropProductID;
        this.cropName = cropName;
        this.cropDescription = cropDescription;
    }

    public String getCropProductID() {
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

    public String getCropDescription() {
        return cropDescription;
    }

    public void setCropDescription(String cropDescription) {
        this.cropDescription = cropDescription;
    }

    public String getCropPrice() {
        return cropPrice;
    }

    public void setCropPrice(String cropPrice) {
        this.cropPrice = cropPrice;
    }

    public String getCropImage() {
        return cropImage;
    }

    public void setCropImage(String cropImage) {
        this.cropImage = cropImage;
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

    public String getUserKey() {
        return userKey;
    }

    public void setUserKey(String userKey) {
        this.userKey = userKey;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getFarmerProfilePic() {
        return farmerProfilePic;
    }

    public void setFarmerProfilePic(String farmerProfilePic) {
        this.farmerProfilePic = farmerProfilePic;
    }
}
