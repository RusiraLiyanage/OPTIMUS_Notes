package com.example.optimusnote;

import com.example.optimusnote.activities.Mainchecklist;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    private Mainchecklist mainchecklist;


    @Before
    public void setup(){
        mainchecklist = new Mainchecklist();
    }
    @Test

    public void addition_isCorrect() {

        assertEquals(4, 2 + 2);
    }
}