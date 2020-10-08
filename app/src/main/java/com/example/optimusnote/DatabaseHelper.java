package com.example.optimusnote;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "IDU.db";
    public static final String TABLE_NAME = "IDU";
    //cols
    public static final String COLS_1 = "ID";
    public static final String COLS_2 = "Title";
    public static final String COLS_3 = "Date";
    public static final String COLS_4 = "Time";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }




    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NAME + "(ID INTEGER PRIMARY KEY AUTOINCREMENT,Title TEXT,Date TEXT,Time TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
    public Cursor ViewData(){
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("Select * from " + TABLE_NAME,null);
        return cursor;
    }
    public Cursor getContact(String ID, SQLiteDatabase sqLiteDatabase){
        String[] projections = {COLS_2,COLS_3,COLS_4};
        String selection = COLS_1 + " LIKE?";
        String[] selection_args = {ID};
        Cursor cursor = sqLiteDatabase.query(TABLE_NAME,projections,selection,selection_args,null,null,null);
        return cursor;
    }
}

