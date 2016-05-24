package com.example.tishinanton.mad2016assignment4;

import android.app.Application;

import com.example.tishinanton.mad2016assignment4.Models.MessageModel;

import java.util.ArrayList;

/**
 * Created by Tishin Anton on 24.05.2016.
 */
public class Assignment4Application extends Application {
    private ArrayList<OnMessageRecievedListener> messageReicevedLisnteres;
    @Override
    public void onCreate() {
        super.onCreate();
        messageReicevedLisnteres = new ArrayList<>();
    }

    public void addMessageRecievedListener(OnMessageRecievedListener listener) {
       if (!messageReicevedLisnteres.contains(listener))
           messageReicevedLisnteres.add(listener);
    }

    public void removeMessageRecievedListener(OnMessageRecievedListener listener) {
        messageReicevedLisnteres.remove(listener);
    }

    public boolean hasMessageRecievedListeners() {
        return messageReicevedLisnteres.size() > 0;
    }

    public void notifyMessageRecievedListeners(MessageModel message) {
        for (OnMessageRecievedListener listener: messageReicevedLisnteres) {
            listener.onMessageResieved(message);
        }
    }
}
