package com.java.yourfarmapp.Model;

public class ChatModel {
    String chatId, receiverId, senderId, receiverName, senderName, message;

    public ChatModel() {
    }

    public ChatModel(String chatId, String receiverId, String senderId, String receiverName, String senderName, String message) {
        this.chatId = chatId;
        this.receiverId = receiverId;
        this.senderId = senderId;
        this.receiverName = receiverName;
        this.senderName = senderName;
        this.message = message;
    }

    public String getChatId() {
        return chatId;
    }

    public void setChatId(String chatId) {
        this.chatId = chatId;
    }

    public String getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(String receiverId) {
        this.receiverId = receiverId;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
