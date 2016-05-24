package com.example.tishinanton.mad2016assignment4.DAL;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Tishin Anton on 24.05.2016.
 */
public class DBHelper extends SQLiteOpenHelper{

    private static final String DATABASE = "Assignment4";
    private static final int VERSION = 1;

    public static final String MESSAGES_TABLE = "Messages";
    public static final String MESSAGES_FIELD_ID = "id";
    public static final String MESSAGES_FIELD_FROM_USER = "fromUser";
    public static final String MESSAGES_FIELD_MESSAGE = "message";

    private Context context;

    public DBHelper(Context context) {
        super(context, DATABASE, null, VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableSql = "CREATE TABLE " + MESSAGES_TABLE + "(" +
                MESSAGES_FIELD_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                MESSAGES_FIELD_FROM_USER + " TEXT NOT NULL," +
                MESSAGES_FIELD_MESSAGE + " TEXT NOT NULL" +
                ")";
        db.execSQL(createTableSql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String dropTableSql = "DROP TABLE " + MESSAGES_TABLE;
        db.execSQL(dropTableSql);
        onCreate(db);
    }
}
