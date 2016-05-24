package com.example.tishinanton.mad2016assignment4.Activities;

import android.content.Intent;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.tishinanton.mad2016assignment4.Helpers.Constants;
import com.example.tishinanton.mad2016assignment4.R;

public class LaunchActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String userName = PreferenceManager.getDefaultSharedPreferences(this).getString(Constants.USER_NAME, null);
        Intent intent;
        if (userName == null) {
             intent = new Intent(this, RegisterActivity.class);
        } else {
            intent = new Intent(this, ChatActivity.class);
        }
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}
