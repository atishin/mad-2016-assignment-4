package com.example.tishinanton.mad2016assignment4.Models;

/**
 * Created by Tishin Anton on 24.05.2016.
 */
public class MessageModel {

    public String fromUser;
    public String message;

    public MessageModel(String fromUser, String message) {
        this.fromUser = fromUser;
        this.message = message;
    }

    public MessageModel() {
        this(null, null);
    }

    @Override
    public String toString() {
        return fromUser + ": " + message;
    }
}
