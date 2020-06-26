package com.java.yourfarmapp.Model;

import java.util.Date;

public class MessagesModel {
    private String toReciever, toSender;
    private String message;
    private String date;
    private String time;

    public MessagesModel() {
    }

    public MessagesModel(String toReciever, String toSender, String message, String date, String time) {
        this.toReciever = toReciever;
        this.toSender = toSender;
        this.message = message;
        this.date = date;
        this.time = time;
    }

    public String getToReciever() {
        return toReciever;
    }

    public void setToReciever(String toReciever) {
        this.toReciever = toReciever;
    }

    public String getToSender() {
        return toSender;
    }

    public void setToSender(String toSender) {
        this.toSender = toSender;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
