package com.hiresight.hiresightclient;

public class ClientMessage {
    private String senderID, receiverID, message, dateTime;

    public ClientMessage() {
    }

    public ClientMessage(String sender, String receiver, String message, String dateTime) {
        this.senderID = sender;
        this.receiverID = receiver;
        this.message = message;
        this.dateTime = dateTime;
    }

    public String getSenderID() {
        return senderID;
    }

    public void setSenderID(String sender) {
        this.senderID = sender;
    }

    public String getReceiverID() {
        return receiverID;
    }

    public void setReceiverID(String receiver) {
        this.receiverID = receiver;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }
}
