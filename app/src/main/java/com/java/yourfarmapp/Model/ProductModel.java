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

    private String productCategoryID;
    private String productCategoryName;
    private String productCategoryImage;

    private String dealerUid;
    private String dealerName;

    List<ProductModel> products;

    public ProductModel() {
    }

    public ProductModel(String cropQuantityType) {
        this.cropQuantityType = cropQuantity;
    }

    public ProductModel(String productCategoryID, String productCategoryName, String productCategoryImage) {
        this.productCategoryID = productCategoryID;
        this.productCategoryName = productCategoryName;
        this.productCategoryImage = productCategoryImage;
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

    public String getProductCategoryID() {
        return productCategoryID;
    }

    public void setProductCategoryID(String productCategoryID) {
        this.productCategoryID = productCategoryID;
    }

    public String getProductCategoryName() {
        return productCategoryName;
    }

    public void setProductCategoryName(String productCategoryName) {
        this.productCategoryName = productCategoryName;
    }

    public String getProductCategoryImage() {
        return productCategoryImage;
    }

    public void setProductCategoryImage(String productCategoryImage) {
        this.productCategoryImage = productCategoryImage;
    }

    public String getDealerUid() {
        return dealerUid;
    }

    public void setDealerUid(String dealerUid) {
        this.dealerUid = dealerUid;
    }

    public String getDealerName() {
        return dealerName;
    }

    public void setDealerName(String dealerName) {
        this.dealerName = dealerName;
    }
}
