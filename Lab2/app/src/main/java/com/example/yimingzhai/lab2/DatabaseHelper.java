package com.example.yimingzhai.lab2;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "food.db";
    private static final String TABLE_NAME = "food_table";
    private static final String ID = "ID";
    private static final String NAME = "NAME";
    private static final String PRICE = "PRICE";
    private static final String DESCRIPTION = "DESCRIPTION";


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
        SQLiteDatabase db = this.getReadableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NAME + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, NAME TEXT, PRICE TEXT, DESCRIPTION TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    // add data
    public boolean addData(String name, String price, String description) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(NAME, name);
        contentValues.put(PRICE, price);
        contentValues.put(DESCRIPTION, description);

        long result = db.insert(TABLE_NAME, null, contentValues);
        if (result == -1) {
            return false;
        }
        else return true;
    }

    // get data
    public Cursor getData() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from " + TABLE_NAME, null);
        return res;
    }

    // delete data
    public boolean deleteData(String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME, "NAME = ?", new String[] {name}) > 0;
    }
}
