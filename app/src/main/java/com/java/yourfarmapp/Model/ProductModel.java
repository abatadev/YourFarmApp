package com.java.yourfarmapp.Model;

public class ProductModel {
    private String productId;
    private String cropType, cropName, cropPrice, cropDescription;

    public ProductModel() {
    }

    public String getProductId() {
        return productId;
    }

    public String getCropDescription() {
        return cropDescription;
    }

    public void setCropDescription(String cropDescription) {
        this.cropDescription = cropDescription;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getCropType() {
        return cropType;
    }

    public void setCropType(String cropType) {
        this.cropType = cropType;
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
}
