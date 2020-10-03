package com.example.optimusnote;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.TextView;

public class ViewDataActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_data);

        DatabaseHelper dbHelper = new DatabaseHelper(this);
        TextView textView = findViewById(R.id.textView5);
        Cursor cursor = dbHelper.ViewData();
        StringBuilder stringBuilder = new StringBuilder();
        while(cursor.moveToNext()){
            stringBuilder.append("\nID: " +cursor.getString(0) + "\nName: " +cursor.getString(1)+"\nDate: " +cursor.getString(2)+"\nTime: " +cursor.getString(3)+"\n") ;
        }
        textView.setText(stringBuilder);

    }
}