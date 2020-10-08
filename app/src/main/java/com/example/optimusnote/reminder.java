package com.example.optimusnote;

import androidx.appcompat.app.AppCompatActivity;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.drawable.AnimationDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.optimusnote.activities.NotMainActivity;
import com.muddzdev.styleabletoastlibrary.StyleableToast;
import com.allyants.notifyme.NotifyMe;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.util.Calendar;

public class reminder extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    Calendar now = Calendar.getInstance();
    TimePickerDialog tpd;
    DatePickerDialog dpd;
    Button buttonInsert, buttonUpdate, buttonDelete, buttonView, button2, buttonRemind;
    EditText TextTitleName, editTextDate2, editTextTime2, textID;
    ImageView imageViewBell;
    private AnimationDrawable anim, anim2, anim3, anim4, anim5,anim6;
    SQLiteOpenHelper openHelper;
    SQLiteDatabase db;
    DatabaseHelper databaseHelper;
    String TheID;


    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        now.set(Calendar.YEAR, year);
        now.set(Calendar.MONTH, monthOfYear);
        now.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        tpd.show(getFragmentManager(), "TimepickerDialog");
    }

    public void onTimeSet(TimePickerDialog view, int hourOfDay, int minute, int second) {
        now.set(Calendar.HOUR_OF_DAY, hourOfDay);
        now.set(Calendar.MINUTE, minute);
        now.set(Calendar.SECOND, second);
        StyleableToast.makeText(getApplicationContext(), "You will be notified ! ", R.style.exampleToast).show();

        NotifyMe notifyMe = new NotifyMe.Builder(getApplicationContext())
                .title(TextTitleName.getText().toString())
                .content("The Date: " + editTextDate2.getText().toString() + "\n" + "The Time: " + editTextTime2.getText().toString() + "\n")
                .color(255, 0, 0, 255)
                .led_color(255, 255, 255, 255)
                .time(now)
                .addAction(new Intent(), "Snooze", false)
                .key("test")
                .addAction(new Intent(), "Dismiss", true, false)
                .addAction(new Intent(), "Done")
                .large_icon(R.mipmap.ic_launcher_round)
                .build();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminder);
        createNotificationChannel();
        buttonInsert = (Button) findViewById(R.id.buttonInsert);
        buttonDelete = (Button) findViewById(R.id.buttonDelete);
        buttonUpdate = (Button) findViewById(R.id.buttonUpdate);
        buttonView = (Button) findViewById(R.id.buttonView);
        button2 = (Button) findViewById(R.id.button2);
        buttonRemind = (Button) findViewById(R.id.buttonRemind);
        textID = (EditText) findViewById(R.id.txtId);
        TextTitleName = (EditText) findViewById(R.id.TextTitleName);
        editTextDate2 = (EditText) findViewById(R.id.editTextDate2);
        editTextTime2 = (EditText) findViewById(R.id.editTextTime2);
        imageViewBell = (ImageView) findViewById(R.id.imageViewBell);
        ImageView imageViewBack = (ImageView) findViewById(R.id.imageViewBack);
        /*anim = (AnimationDrawable) buttonInsert.getBackground();
        anim.setEnterFadeDuration(2300);
        anim.setExitFadeDuration(2300);

        anim2 = (AnimationDrawable) buttonUpdate.getBackground();
        anim2.setEnterFadeDuration(2300);
        anim2.setExitFadeDuration(2300);

        anim3 = (AnimationDrawable) buttonDelete.getBackground();
        anim3.setEnterFadeDuration(2300);
        anim3.setExitFadeDuration(2300);

        anim4 = (AnimationDrawable) buttonView.getBackground();
        anim4.setEnterFadeDuration(150);
        anim4.setExitFadeDuration(150);

        anim5 = (AnimationDrawable) button2.getBackground();
        anim5.setEnterFadeDuration(2300);
        anim5.setExitFadeDuration(2300);

        anim6 = (AnimationDrawable) buttonRemind.getBackground();
        anim6.setEnterFadeDuration(150);
        anim6.setExitFadeDuration(150);*/
        
        dpd = DatePickerDialog.newInstance(
                reminder.this,
                now.get(Calendar.YEAR),
                now.get(Calendar.MONTH),
                now.get(Calendar.DAY_OF_MONTH)

        );

        tpd = TimePickerDialog.newInstance(
                reminder.this,
                now.get(Calendar.HOUR_OF_DAY),
                now.get(Calendar.MINUTE),
                now.get(Calendar.SECOND),
                false
        );

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String Text1 = TextTitleName.getText().toString();
                String Text2 = editTextDate2.getText().toString();
                String Text3 = editTextTime2.getText().toString();
                if(Text1.isEmpty() && Text2.isEmpty() && Text3.isEmpty()){
                    StyleableToast.makeText(getApplicationContext(), "All fields are already empty!", R.style.exampleToast).show();
                }
                else {
                    TextTitleName.setText("");
                    editTextDate2.setText("");
                    editTextTime2.setText("");
                    StyleableToast.makeText(getApplicationContext(), "All fields are Cleared!", R.style.exampleToast).show();
                }
            }
        });

        imageViewBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), NotMainActivity.class);
                startActivity(intent);
            }
        });

        buttonRemind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dpd.show(getFragmentManager(),"DatepickerDialog");
            }
        });


        Button viewData = findViewById(R.id.buttonView);

        viewData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ViewDataActivity.class);
                StyleableToast.makeText(getApplicationContext(), "SHOWING ALL REMINDERS!", R.style.exampleToast).show();

                startActivity(intent);
            }
        });





        openHelper = new DatabaseHelper(this);
        buttonInsert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = TextTitleName.getText().toString();
                String date = editTextDate2.getText().toString();
                String time = editTextTime2.getText().toString();
                db = openHelper.getWritableDatabase();
                insertData(name, date, time);
                StyleableToast.makeText(getApplicationContext(), "INSERTED SUCCESSFULLY", R.style.exampleToast).show();

            }
        });
        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db = openHelper.getWritableDatabase();
                String id = textID.getText().toString();
                deleteData(id);
                StyleableToast.makeText(getApplicationContext(), "Deleted Successfully", R.style.exampleToast).show();
            }
        });
        buttonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String id = textID.getText().toString();

                db = openHelper.getWritableDatabase();

                String name = TextTitleName.getText().toString();

                String date = editTextDate2.getText().toString();

                String time = editTextTime2.getText().toString();

                updateData(name, date, time);
                StyleableToast.makeText(getApplicationContext(), "UPDATED SUCCESSFULLY", R.style.exampleToast).show();
            }
        });


    }


    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "NotificationChannel";
            String description = "Channel for Reminders";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("Nothing", name, importance);
        }
    }

    public void insertData(String name, String date, String time) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseHelper.COLS_2, name);
        contentValues.put(DatabaseHelper.COLS_3, date);
        contentValues.put(DatabaseHelper.COLS_4, time);

        long id = db.insert(DatabaseHelper.TABLE_NAME, null, contentValues);
    }

    public boolean deleteData(String id) {
        return db.delete(DatabaseHelper.TABLE_NAME, DatabaseHelper.COLS_1 + "=?", new String[]{id}) > 0;
    }

    public boolean updateData(String name, String date, String time) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseHelper.COLS_2, name);
        contentValues.put(DatabaseHelper.COLS_3, date);
        contentValues.put(DatabaseHelper.COLS_4, time);
        String id = textID.getText().toString();
        return db.update(DatabaseHelper.TABLE_NAME, contentValues, DatabaseHelper.COLS_1 + "=?", new String[]{id}) > 0;
    }

    public void SearchContact(View view){
        TheID = textID.getText().toString();
        databaseHelper = new DatabaseHelper(getApplicationContext());
        db = databaseHelper.getReadableDatabase();
        Cursor cursor = databaseHelper.getContact(TheID,db);
        if(cursor.moveToNext())
        {
            String Title = cursor.getString(cursor.getColumnIndex("Title"));
            String Date = cursor.getString(cursor.getColumnIndex("Date"));
            String Time = cursor.getString(cursor.getColumnIndex("Time"));
            TextTitleName.setText(Title);
            editTextDate2.setText(Date);
            editTextTime2.setText(Time);

        }
        else{
            StyleableToast.makeText(getApplicationContext(), "Invalid ID ! ", R.style.exampleToast).show();
        }
    }
/*
    @Override
    protected void onResume() {
        super.onResume();
        if (anim != null && !anim.isRunning()) {
            anim.start();
        }
        if (anim2 != null && !anim2.isRunning()) {
            anim2.start();
        }
        if (anim3 != null && !anim3.isRunning()) {
            anim3.start();
        }
        if (anim4 != null && !anim4.isRunning()) {
            anim4.start();
        }
        if (anim5 != null && !anim5.isRunning()) {
            anim5.start();
        }
        if (anim6 != null && !anim6.isRunning()) {
            anim6.start();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (anim != null && !anim.isRunning()) {
            anim.stop();
        }
        if (anim2 != null && !anim2.isRunning()) {
            anim2.stop();
        }
        if (anim3 != null && !anim3.isRunning()) {
            anim3.stop();
        }
        if (anim4 != null && !anim4.isRunning()) {
            anim4.stop();
        }
        if (anim5 != null && !anim5.isRunning()) {
            anim5.stop();
        }
        if (anim6 != null && !anim6.isRunning()) {
            anim6.stop();
        }
    }*/

}
