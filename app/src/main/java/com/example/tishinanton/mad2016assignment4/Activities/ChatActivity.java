package com.example.tishinanton.mad2016assignment4.Activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.example.tishinanton.mad2016assignment4.Assignment4Application;
import com.example.tishinanton.mad2016assignment4.DAL.MessagesRepository;
import com.example.tishinanton.mad2016assignment4.Helpers.Constants;
import com.example.tishinanton.mad2016assignment4.Models.GCMRequestModel;
import com.example.tishinanton.mad2016assignment4.Models.MessageModel;
import com.example.tishinanton.mad2016assignment4.OnMessageRecievedListener;
import com.example.tishinanton.mad2016assignment4.R;
import com.example.tishinanton.mad2016assignment4.Services.RegistrationIntentService;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.ByteArrayEntity;
import cz.msebera.android.httpclient.entity.ContentType;
import cz.msebera.android.httpclient.entity.StringEntity;
import cz.msebera.android.httpclient.message.BasicHeader;
import cz.msebera.android.httpclient.protocol.HTTP;

public class ChatActivity extends AppCompatActivity implements OnMessageRecievedListener {

    private ListView chatListView;
    private EditText chatMessageEditText;
    private Button sendButton;

    private String userName;

    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;

    private BroadcastReceiver mRegistrationBroadcastReceiver;
    private boolean isReceiverRegistered = false;
    private ArrayList<MessageModel> messages;
    private ArrayAdapter<MessageModel> adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        chatListView = (ListView) findViewById(R.id.activity_chat_chatListView);
        chatMessageEditText = (EditText) findViewById(R.id.activity_chat_textMessageEditText);
        sendButton = (Button) findViewById(R.id.activity_chat_sendButton);

        messages = new ArrayList<>();

        MessagesRepository repository = new MessagesRepository(this);
        for (MessageModel message : repository.get(10, 0)) {
            messages.add(message);
        }
        adapter = new ArrayAdapter<MessageModel>(this, android.R.layout.simple_expandable_list_item_1, messages);
        chatListView.setAdapter(adapter);

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSendButtonClicked();
            }
        });
        sendButton.setEnabled(false);

        userName = PreferenceManager.getDefaultSharedPreferences(this).getString(Constants.USER_NAME, null);
        if (userName == null) {
            Intent intent = new Intent(this, RegisterActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        }

        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                broadCastRecieved(intent);
            }
        };

        registerReceiver();

        if (checkPlayServices()) {
            Intent intent = new Intent(this, RegistrationIntentService.class);
            startService(intent);
        }
        ((Assignment4Application)getApplication()).addMessageRecievedListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver();
        ((Assignment4Application)getApplication()).addMessageRecievedListener(this);
    }

    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        isReceiverRegistered = false;
        ((Assignment4Application)getApplication()).removeMessageRecievedListener(this);
        super.onPause();
    }

    private void registerReceiver(){
        if(!isReceiverRegistered) {
            LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                    new IntentFilter(Constants.REGISTRATION_COMPLETE));
            isReceiverRegistered = true;
        }
    }

    private void broadCastRecieved(Intent intent) {
        if (PreferenceManager.getDefaultSharedPreferences(this).getBoolean(Constants.SEND_TOKEN_TO_SERVER, false)) {
            sendButton.setEnabled(true);
        }
    }

    private void onSendButtonClicked() {
        String message = chatMessageEditText.getText().toString();
        if (message.length() > 0) {
            MessageModel messageModel = new MessageModel(userName, message);
            GCMRequestModel requestModel = new GCMRequestModel(messageModel);

            AsyncHttpClient client = new AsyncHttpClient();
            client.addHeader("Authorization", "key=" + Constants.API_KEY);

            ByteArrayEntity entity = null;
            try {
                entity = new ByteArrayEntity(requestModel.toJsonString().getBytes("UTF-8"));
                entity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                return;
            }

            client.post(this, "https://gcm-http.googleapis.com/gcm/send", entity, "application/json", new AsyncHttpResponseHandler() {

                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                    String response = new String(responseBody, Charset.forName("UTF-8"));
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                }
            });

        } else {
            chatMessageEditText.setError("Message length must be more than zero");
        }
    }

    private boolean checkPlayServices() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(this, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST)
                        .show();
            } else {
                Log.i("checkPlayServices", "This device is not supported.");
                finish();
            }
            return false;
        }
        return true;
    }

    @Override
    public void onMessageResieved(final MessageModel message) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                messages.add(message);
                adapter.notifyDataSetChanged();
            }
        });
    }
}
