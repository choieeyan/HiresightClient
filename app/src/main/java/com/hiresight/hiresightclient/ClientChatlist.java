package com.hiresight.hiresightclient;

public class ClientChatlist {
    private String recentMessage, timeStamp, userID;

    public ClientChatlist() {
    }

    public ClientChatlist(String recentMessage, String timeStamp, String userID) {
        this.recentMessage = recentMessage;
        this.timeStamp = timeStamp;
        this.userID = userID;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getRecentMessage() {
        return recentMessage;
    }

    public void setRecentMessage(String recentMessage) {
        this.recentMessage = recentMessage;
    }

}
