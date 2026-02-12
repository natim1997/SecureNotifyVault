package com.example.securenotifyvault;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "VaultDB";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_NAME = "notifications";

    private static final String COL_ID = "id";
    private static final String COL_APP = "app_name";
    private static final String COL_TITLE = "title";
    private static final String COL_TEXT = "content";
    private static final String COL_TIME = "timestamp";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_NAME + " (" +
                COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_APP + " TEXT, " +
                COL_TITLE + " TEXT, " +
                COL_TEXT + " TEXT, " +
                COL_TIME + " LONG)";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public void addNotification(String appName, String title, String text) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        String encryptedText = EncryptionHelper.encrypt(text);

        values.put(COL_APP, appName);
        values.put(COL_TITLE, title);
        values.put(COL_TEXT, encryptedText);
        values.put(COL_TIME, System.currentTimeMillis());

        db.insert(TABLE_NAME, null, values);
        db.close();
    }

    public void deleteAll() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_NAME);
        db.close();
    }

    public static class NotificationItem {
        public String app, title, text, time;
        public NotificationItem(String app, String title, String text, String time) {
            this.app = app; this.title = title; this.text = text; this.time = time;
        }
    }

    public ArrayList<NotificationItem> getAllNotifications() {
        ArrayList<NotificationItem> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME + " ORDER BY " + COL_ID + " DESC", null);

        if (cursor.moveToFirst()) {
            do {
                String app = cursor.getString(cursor.getColumnIndexOrThrow(COL_APP));
                String title = cursor.getString(cursor.getColumnIndexOrThrow(COL_TITLE));
                String encryptedText = cursor.getString(cursor.getColumnIndexOrThrow(COL_TEXT));
                long time = cursor.getLong(cursor.getColumnIndexOrThrow(COL_TIME));

                String realText = EncryptionHelper.decrypt(encryptedText);

                list.add(new NotificationItem(app, title, realText, String.valueOf(time)));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return list;
    }
}