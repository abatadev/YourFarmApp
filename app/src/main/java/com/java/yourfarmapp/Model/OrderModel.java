package com.java.yourfarmapp.Model;

public class OrderModel {

    private String farmerId, dealerId;
    private String productId, productName, productDescription, productPicture;
    private String orderId;
    private String farmerName, farmerContactNumber, farmerPicture;
    private String dealerName, dealerContactNumber, dealerPicture;
    private String productCategory;

    private String orderDate;
    private String orderTime;

    private String productPrice;
    private String productQuantity;
    private String productTotalPrice;

    private boolean isComplete;

    public OrderModel() {
    }

    public OrderModel(String farmerId, String dealerId, String productId, String productName, String productDescription, String productPicture, String orderId, String farmerName, String farmerContactNumber, String farmerPicture, String dealerName, String dealerContactNumber, String dealerPicture, String productCategory, String orderDate, String orderTime, String productPrice, String productQuantity, String productTotalPrice, boolean isComplete) {
        this.farmerId = farmerId;
        this.dealerId = dealerId;
        this.productId = productId;
        this.productName = productName;
        this.productDescription = productDescription;
        this.productPicture = productPicture;
        this.orderId = orderId;
        this.farmerName = farmerName;
        this.farmerContactNumber = farmerContactNumber;
        this.farmerPicture = farmerPicture;
        this.dealerName = dealerName;
        this.dealerContactNumber = dealerContactNumber;
        this.dealerPicture = dealerPicture;
        this.productCategory = productCategory;
        this.orderDate = orderDate;
        this.orderTime = orderTime;
        this.productPrice = productPrice;
        this.productQuantity = productQuantity;
        this.productTotalPrice = productTotalPrice;
        this.isComplete = isComplete;
    }

    public String getFarmerId() {
        return farmerId;
    }

    public void setFarmerId(String farmerId) {
        this.farmerId = farmerId;
    }

    public String getDealerId() {
        return dealerId;
    }

    public void setDealerId(String dealerId) {
        this.dealerId = dealerId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
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

    public String getProductPicture() {
        return productPicture;
    }

    public void setProductPicture(String productPicture) {
        this.productPicture = productPicture;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
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

    public String getProductCategory() {
        return productCategory;
    }

    public void setProductCategory(String productCategory) {
        this.productCategory = productCategory;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public String getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(String orderTime) {
        this.orderTime = orderTime;
    }

    public String getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(String productPrice) {
        this.productPrice = productPrice;
    }

    public String getProductQuantity() {
        return productQuantity;
    }

    public void setProductQuantity(String productQuantity) {
        this.productQuantity = productQuantity;
    }

    public String getProductTotalPrice() {
        return productTotalPrice;
    }

    public void setProductTotalPrice(String productTotalPrice) {
        this.productTotalPrice = productTotalPrice;
    }

    public boolean isComplete() {
        return isComplete;
    }

    public void setComplete(boolean complete) {
        isComplete = complete;
    }
}
