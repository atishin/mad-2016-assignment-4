package com.example.tishinanton.mad2016assignment4.Services;

import android.app.IntentService;
import android.content.Intent;
import android.preference.PreferenceManager;
import android.support.v4.content.LocalBroadcastManager;

import com.example.tishinanton.mad2016assignment4.Helpers.Constants;
import com.example.tishinanton.mad2016assignment4.R;
import com.google.android.gms.gcm.GcmPubSub;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;

import java.io.IOException;

/**
 * Created by Tishin Anton on 24.05.2016.
 */
public class RegistrationIntentService extends IntentService {

    private static final String TAG = "RegistrationIntentService";

    public RegistrationIntentService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        InstanceID instanceID = InstanceID.getInstance(this);
        try {
            String token = instanceID.getToken(getString(R.string.gcm_defaultSenderId),
                    GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);
            PreferenceManager.getDefaultSharedPreferences(this).edit().putBoolean(Constants.SEND_TOKEN_TO_SERVER, true).apply();
            subscribeTopics(token);
            Intent registrationComplete = new Intent(Constants.REGISTRATION_COMPLETE);
            LocalBroadcastManager.getInstance(this).sendBroadcast(registrationComplete);
        } catch (IOException e) {
            PreferenceManager.getDefaultSharedPreferences(this).edit().putBoolean(Constants.SEND_TOKEN_TO_SERVER, false).apply();
            e.printStackTrace();
        }
    }

    private void subscribeTopics(String token) throws IOException {
        GcmPubSub pubSub = GcmPubSub.getInstance(this);
        for (String topic : Constants.TOPICS) {
            pubSub.subscribe(token, "/topics/" + topic, null);
        }
    }
}
