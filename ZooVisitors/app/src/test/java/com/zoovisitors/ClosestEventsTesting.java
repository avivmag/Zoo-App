package com.zoovisitors;

import org.junit.Test;

import static org.junit.Assert.*;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */

public class ClosestEventsTesting {
    private AppCompatActivity appCompatActivity = new AppCompatActivity();

    @Test
    public void rightClosest() throws Exception {
        assertEquals("Closes event: 14:00-15:00 Dancing",
                ((TextView) appCompatActivity.findViewById(R.id.closesEvent)).getText());
    }
}