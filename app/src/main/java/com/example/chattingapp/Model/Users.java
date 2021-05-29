package com.example.chattingapp.Model;

public class Users {

    private String id;
    private String username;
    private String password;
    private String email;
    private String imageURL;
    private String about;
    private String search;

    //constructors
    public Users() {
    }

    public Users(String id, String username, String imageURL, String password, String email, String search) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.email = email;
        this.imageURL = imageURL;
        this.about = about;
        this.search = search;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public void setSearch(String search){this.search = search;}

    public String getSearch(){return search;}

}
