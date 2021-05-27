package com.example.chattingapp;

public class UsersModel {

    private String id;
    private String username;
    private String imageURL;

    //constructor
    public UsersModel() {
    }

    public UsersModel(String id, String username, String imageURL) {
        this.id = id;
        this.id = username;
        this.imageURL = imageURL;
    }

    //getters and setters
    public  String getId(){
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public  String getUsername(){
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }
}
