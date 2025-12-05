package edu.csumb.cst338.otterbots.rockpaperscissors.database.entities;

import android.content.Context;
import android.util.Log;

import androidx.test.core.app.ApplicationProvider;

import junit.framework.TestCase;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;


import java.io.IOException;

@RunWith(androidx.test.ext.junit.runners.AndroidJUnit4.class)
public class RockPaperScissorsDatabaseTest {
    private RockPaperScissorsDatabase db;

    @Before
    public void createDb() {
        Context context = ApplicationProvider.getApplicationContext();
        db = RockPaperScissorsDatabase.getDatabase(context);
    }

    @After
    public void closeDb() throws IOException {
        db.close();
    }

    @Test
    public void testGetDatabase() throws Exception{
        System.out.println("Testing database creation and factory");
        assert(db != null);
    }

    //TODO: test the DAOs here
}