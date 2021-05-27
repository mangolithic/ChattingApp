package com.example.chattingapp.Model;

public class Users {

    private String id;
    private String username;
    private String imageURL;

    //constructors
    public Users() {
    }

    public Users(String id, String username, String imageURL) {
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
