package com.hiresight.hiresightclient;

import java.util.Locale;

public class CreateJobPost {
    private String postDateTime, startDate, endDate, location, product, pay, paxRequired, profession, clientID;

    public CreateJobPost(String postDateTime, String startDate, String endDate, String location, String product, String pay, String paxRequired, String profession, String clientID) {
        this.postDateTime = postDateTime;
        this.startDate = startDate;
        this.endDate = endDate;
        this.location = location;
        this.product = product;
        this.pay = pay;
        this.paxRequired = paxRequired;
        this.profession = profession;
        this.clientID = clientID;
    }

    public String getPostDateTime() {
        return postDateTime;
    }

    public void setPostDateTime(String postDateTime) {
        this.postDateTime = postDateTime;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public String getPay() {
        return pay;
    }

    public void setPay(String pay) {
        this.pay = pay;
    }

    public String getPaxRequired() {
        return paxRequired;
    }

    public void setPaxRequired(String paxRequired) {
        this.paxRequired = paxRequired;
    }

    public String getProfession() {
        return profession;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }

    public String getClientID() {
        return clientID;
    }

    public void setClientID(String clientID) {
        this.clientID = clientID;
    }
}
