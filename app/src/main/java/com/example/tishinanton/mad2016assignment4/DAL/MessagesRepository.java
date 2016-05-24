package com.example.tishinanton.mad2016assignment4.DAL;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.tishinanton.mad2016assignment4.Models.MessageModel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

/**
 * Created by Tishin Anton on 24.05.2016.
 */
public class MessagesRepository {

    private Context context;

    public MessagesRepository(Context context) {
        this.context = context;
    }

    public ArrayList<MessageModel> get(int count, int offset) {
        ArrayList<MessageModel> messages = new ArrayList<>();

        DBHelper dbHelper = new DBHelper(context);

        try {
            SQLiteDatabase db = dbHelper.getReadableDatabase();
            Cursor cursor = db.query(DBHelper.MESSAGES_TABLE, null, null, null, null, null, DBHelper.MESSAGES_FIELD_ID + " DESC", offset + "," + count);
            if (cursor.moveToFirst()) {
                do {
                    MessageModel message = new MessageModel();
                    message.fromUser = cursor.getString(cursor.getColumnIndex(DBHelper.MESSAGES_FIELD_FROM_USER));
                    message.message = cursor.getString(cursor.getColumnIndex(DBHelper.MESSAGES_FIELD_MESSAGE));
                    messages.add(message);
                } while (cursor.moveToNext());
            }
        } finally {
            dbHelper.close();
        }

        Collections.reverse(messages);
        return messages;
    }

    public void add(MessageModel message) {
        DBHelper dbHelper = new DBHelper(context);
        try {
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(DBHelper.MESSAGES_FIELD_MESSAGE, message.message);
            values.put(DBHelper.MESSAGES_FIELD_FROM_USER, message.fromUser);
            db.insert(DBHelper.MESSAGES_TABLE, null, values);
        } finally {
            dbHelper.close();
        }
    }

}
