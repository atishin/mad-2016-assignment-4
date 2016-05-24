package com.example.tishinanton.mad2016assignment4.Services;

import android.content.Intent;

import com.google.android.gms.iid.InstanceIDListenerService;

/**
 * Created by Tishin Anton on 24.05.2016.
 */
public class MyInstanceIDListenerService extends InstanceIDListenerService {

    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();
        Intent intent = new Intent(this, RegistrationIntentService.class);
        startService(intent);
    }
}
