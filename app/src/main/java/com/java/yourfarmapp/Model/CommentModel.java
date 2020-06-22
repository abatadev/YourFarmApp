package com.java.yourfarmapp.Model;

public class CommentModel {
    private String dealerUid = "";
    private String comment = "";
    private String date = "";
    private String rating = "";

    public CommentModel() {
    }

    public CommentModel(String dealerUid, String comment, String date, String rating) {
        this.dealerUid = dealerUid;
        this.comment = comment;
        this.date = date;
        this.rating = rating;
    }

    public String getDealerUid() {
        return dealerUid;
    }

    public void setDealerUid(String dealerUid) {
        this.dealerUid = dealerUid;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }
}
