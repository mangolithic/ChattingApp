package com.example.chattingapp.Model;

import android.net.Uri;

public class Chat {
    private String sender;
    private String receiver;
    private String message;
    private String URL;
    private String type;

    public Chat(String sender, String receiver, String message, String URL, String type) {
        this.sender = sender;
        this.receiver = receiver;
        this.message = message;
        this.URL = URL;
        this.type = type;
    }

    public Chat() {
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setURL(String URL) {this.URL = URL; }

    public String getURL() {return URL;}

    public void setType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
