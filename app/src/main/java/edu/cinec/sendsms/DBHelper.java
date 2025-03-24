package edu.cinec.sendsms;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "UserDB.db";
    public static final String TABLE_NAME = "users";
    public static final String COL_ID = "ID";
    public static final String COL_USERNAME = "USERNAME";
    public static final String COL_PASSWORD = "PASSWORD";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NAME + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, USERNAME TEXT UNIQUE, PASSWORD TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    // Insert new User
    public boolean insertUser(String username, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_USERNAME, username);
        values.put(COL_PASSWORD, password);

        long result = db.insert(TABLE_NAME, null, values);
        return result != -1; // Return true if inserted successfully
    }

    // Check the user is already exist
    public boolean checkUser(String username, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE USERNAME=? AND PASSWORD=?", new String[]{username, password});
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        return exists;
    }

    // get all users
    public Cursor getAllUsers() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
    }
}
