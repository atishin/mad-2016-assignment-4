package com.example.tishinanton.mad2016assignment4.Activities;

import android.content.Intent;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.tishinanton.mad2016assignment4.Helpers.Constants;
import com.example.tishinanton.mad2016assignment4.R;

public class RegisterActivity extends AppCompatActivity {

    private EditText userNameEditText;
    private Button registerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        userNameEditText = (EditText)findViewById(R.id.activity_register_userName);
        registerButton = (Button) findViewById(R.id.activity_register_registerButton);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onRegisterButtonClicked();
            }
        });

    }

    private void onRegisterButtonClicked() {
        String userName = userNameEditText.getText().toString();
        if (userName.length() > 0) {
            PreferenceManager.getDefaultSharedPreferences(this).edit().putString(Constants.USER_NAME, userName).apply();
            Intent intent = new Intent(this, ChatActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        } else {
            userNameEditText.setError("Length of the name should be more than zero");
        }
    }
}
