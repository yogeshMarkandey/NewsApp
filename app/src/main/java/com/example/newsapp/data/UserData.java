package com.example.newsapp.data;

public class UserData {
    private String uId;
    private String name;
    private String emailId;
    private String userName;
    private String avatar;

    public UserData() {
        //Empty Constructor
    }

    public UserData(String uId, String name, String emailId, String userName, String avatar) {
        this.uId = uId;
        this.name = name;
        this.emailId = emailId;
        this.userName = userName;
        this.avatar = avatar;
    }

    // Getter And Setter
    public String getuId() {
        return uId;
    }

    public void setuId(String uId) {
        this.uId = uId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
}
