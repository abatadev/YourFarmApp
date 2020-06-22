package com.java.yourfarmapp.Model;

public class Orders {
    private String productName, productDescription, productQuantity, productPrice, productPicture;

    private String farmerName, farmerContactNumber, farmerPicture;
    private String dealerName, dealerContactNumber, dealerPicture;

    private String dataStart, dateFinished;

    private boolean isComplete;

    public Orders() {
    }

    public Orders(String productName, String productDescription, String productQuantity, String productPrice, String productPicture,
                  String farmerName, String farmerContactNumber, String farmerPicture,
                  String dealerName, String dealerContactNumber, String dealerPicture,
                  String dataStart, String dateFinished,
                  boolean isComplete) {

        this.productName = productName;
        this.productDescription = productDescription;
        this.productQuantity = productQuantity;
        this.productPrice = productPrice;
        this.productPicture = productPicture;
        this.farmerName = farmerName;
        this.farmerContactNumber = farmerContactNumber;
        this.farmerPicture = farmerPicture;
        this.dealerName = dealerName;
        this.dealerContactNumber = dealerContactNumber;
        this.dealerPicture = dealerPicture;
        this.dataStart = dataStart;
        this.dateFinished = dateFinished;
        this.isComplete = isComplete;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductDescription() {
        return productDescription;
    }

    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
    }

    public String getProductQuantity() {
        return productQuantity;
    }

    public void setProductQuantity(String productQuantity) {
        this.productQuantity = productQuantity;
    }

    public String getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(String productPrice) {
        this.productPrice = productPrice;
    }

    public String getProductPicture() {
        return productPicture;
    }

    public void setProductPicture(String productPicture) {
        this.productPicture = productPicture;
    }

    public String getFarmerName() {
        return farmerName;
    }

    public void setFarmerName(String farmerName) {
        this.farmerName = farmerName;
    }

    public String getFarmerContactNumber() {
        return farmerContactNumber;
    }

    public void setFarmerContactNumber(String farmerContactNumber) {
        this.farmerContactNumber = farmerContactNumber;
    }

    public String getFarmerPicture() {
        return farmerPicture;
    }

    public void setFarmerPicture(String farmerPicture) {
        this.farmerPicture = farmerPicture;
    }

    public String getDealerName() {
        return dealerName;
    }

    public void setDealerName(String dealerName) {
        this.dealerName = dealerName;
    }

    public String getDealerContactNumber() {
        return dealerContactNumber;
    }

    public void setDealerContactNumber(String dealerContactNumber) {
        this.dealerContactNumber = dealerContactNumber;
    }

    public String getDealerPicture() {
        return dealerPicture;
    }

    public void setDealerPicture(String dealerPicture) {
        this.dealerPicture = dealerPicture;
    }

    public String getDataStart() {
        return dataStart;
    }

    public void setDataStart(String dataStart) {
        this.dataStart = dataStart;
    }

    public String getDateFinished() {
        return dateFinished;
    }

    public void setDateFinished(String dateFinished) {
        this.dateFinished = dateFinished;
    }

    public boolean isComplete() {
        return isComplete;
    }

    public void setComplete(boolean complete) {
        isComplete = complete;
    }
}
