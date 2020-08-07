package com.java.yourfarmapp.Model;

public class RatingModel {
    private String ratingId, dealerId, farmerId, dealerName, farmerName, comment;
    private float ratingValue;
    private int ratingCount;

    public RatingModel() {
    }

    public RatingModel(String ratingId, String dealerId, String farmerId, String dealerName, String farmerName, String comment, float ratingValue, int ratingCount) {
        this.ratingId = ratingId;
        this.dealerId = dealerId;
        this.farmerId = farmerId;
        this.dealerName = dealerName;
        this.farmerName = farmerName;
        this.comment = comment;
        this.ratingValue = ratingValue;
        this.ratingCount = ratingCount;
    }

    public String getRatingId() {
        return ratingId;
    }

    public void setRatingId(String ratingId) {
        this.ratingId = ratingId;
    }

    public String getDealerId() {
        return dealerId;
    }

    public void setDealerId(String dealerId) {
        this.dealerId = dealerId;
    }

    public String getFarmerId() {
        return farmerId;
    }

    public void setFarmerId(String farmerId) {
        this.farmerId = farmerId;
    }

    public String getDealerName() {
        return dealerName;
    }

    public void setDealerName(String dealerName) {
        this.dealerName = dealerName;
    }

    public String getFarmerName() {
        return farmerName;
    }

    public void setFarmerName(String farmerName) {
        this.farmerName = farmerName;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public float getRatingValue() {
        return ratingValue;
    }

    public void setRatingValue(float ratingValue) {
        this.ratingValue = ratingValue;
    }

    public int getRatingCount() {
        return ratingCount;
    }

    public void setRatingCount(int ratingCount) {
        this.ratingCount = ratingCount;
    }
}
