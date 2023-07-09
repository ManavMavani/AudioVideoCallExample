package com.example.audiovideocallexample;

import java.util.Map;

public class User {
    private String uId, userName, phoneNumber;
    private Map<String, Object> friends;

    public User() {
    }

    public User(String uId, String userName, String phoneNumber) {
        this.uId = uId;
        this.userName = userName;
        this.phoneNumber = phoneNumber;
    }

    public String getuId() {
        return uId;
    }

    public void setuId(String uId) {
        this.uId = uId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Map<String, Object> getFriends() {
        return friends;
    }

    public void setFriends(Map<String, Object> friends) {
        this.friends = friends;
    }
}
