package com.hiresight.hiresightclient;

import java.util.Date;

public class User {
    private String name, gender, email, password, contact, imageURL;
    private int age, years;
    public User(){

    }

    public User(String name, int age, String gender, String contact, int years, String email, String password, String imageURL) {
        this.name = name;
        this.gender = gender;
        this.email = email;
        this.password = password;
        this.contact = contact;
        this.years = years;
        this.age = age;
        this.imageURL = imageURL;
    }



    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getYears() {
        return years;
    }

    public void setYears(int years) {
        this.years = years;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }
}
