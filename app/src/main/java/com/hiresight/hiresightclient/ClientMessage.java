package com.hiresight.hiresightclient;

import java.util.Date;

public class ClientMessage {
    private String senderID, receiverID, message;
    private Date dateTime;

    public ClientMessage() {
    }

    public ClientMessage(String sender, String receiver, String message, Date dateTime) {
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

    public Date getDateTime() {
        return dateTime;
    }

    public void setDateTime(Date dateTime) {
        this.dateTime = dateTime;
    }
}
