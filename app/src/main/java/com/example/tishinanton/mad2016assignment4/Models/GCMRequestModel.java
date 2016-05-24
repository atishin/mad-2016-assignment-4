package com.example.tishinanton.mad2016assignment4.Models;

import com.example.tishinanton.mad2016assignment4.Helpers.Constants;
import com.google.gson.Gson;

/**
 * Created by Tishin Anton on 24.05.2016.
 */
public class GCMRequestModel {

    public String to;
    public MessageModel data;

    public GCMRequestModel(String to, MessageModel data) {
        this.to = to;
        this.data = data;
    }

    public GCMRequestModel(MessageModel data) {
        this.to = Constants.MAIN_TOPIC;
        this.data = data;
    }

    public GCMRequestModel() {
        this(null);
    }

    @Override
    public String toString() {
        return data.toString();
    }

    public String toJsonString() {
        return new Gson().toJson(this).toString();
    }
}
