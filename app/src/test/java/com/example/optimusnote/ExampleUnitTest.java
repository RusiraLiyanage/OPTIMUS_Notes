package com.example.optimusnote;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    SQLiteDatabase db;
    SQLiteOpenHelper openHelper;

    private reminder Reminder;
    @Before
            public void setUp(){
        Reminder = new reminder();
    }

    @Test
    public void addReminder(){

        String Title = "The Test Reminder";
        String Date = "10/04/2020";
        String Time = "7.35 AM";
        db = openHelper.getWritableDatabase();
        boolean Status = db.insertData(Title, Date, Time);
        assertTrue(Status);
    }

    @Test
    public void DeleteReminder(){
        String name = "Test case for delete";
        db = openHelper.getWritableDatabase();
        db.addSubject(name,db);
        boolean Status = db.DeleteData(name);
        assertTrue(Status);
    }

    public void UpdateReminder(){
        String Title = "The reminder Test";
        String Date = "12/04/2020";
        String Time = "4.34 p.m";
        db = openHelper.getWritableDatabase();
        boolean Status = db.UpdateData(Title,Date,Time);
        assertTrue(Status);
    }


    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }


}