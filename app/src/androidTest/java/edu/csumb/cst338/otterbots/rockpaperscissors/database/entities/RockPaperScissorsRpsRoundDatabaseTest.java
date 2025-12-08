package edu.csumb.cst338.otterbots.rockpaperscissors.database.entities;



import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import android.app.Application;
import android.content.Context;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.LiveData;
import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;


import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;


/**
 * Run the db tests for RpsRound
 * Author: Josh Goldberg
 * Since: 2025.12.06
 */
@RunWith(androidx.test.ext.junit.runners.AndroidJUnit4.class)
public class RockPaperScissorsRpsRoundDatabaseTest {
    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    private TestRepository repository;

    TestLiveDataObserver<ArrayList<RpsRound>> rpsRoundTestObserver;

    RpsRound testRound1;
    RpsRound testRound2;
    RpsRound testRound3;



    @Before
    public void beforeEach() {
        Context context = ApplicationProvider.getApplicationContext();
        repository = new TestRepository((Application) context);

        assertNotNull(repository);

        // create an observer for livedata tests. see the helper class below.
        rpsRoundTestObserver = new TestLiveDataObserver<>();

        testRound1 = new RpsRound(1,2,3,"win");
        testRound2 = new RpsRound(1,3,3,"tie");
        testRound3 = new RpsRound(2,1,2,"win");
    }

    @After
    public void afterEach() throws IOException {
        repository = null;
    }


    /**
     * test a single record insert
     */
    @Test
    public void testRpsRound() throws Exception {
        repository.insertRound(testRound1);

        // get the data
        LiveData<ArrayList<RpsRound>> lookupRoundLiveData = repository.getAllUserStatsIdRounds(1);

        // this handler for the livedata observer runs our tests
        LiveDataOnChangedHandler<ArrayList<RpsRound>> handler = data -> {
            assertNotNull(data);
            assertEquals(1, data.size());
            assertEquals("win", data.get(0).getResult());
        };
        // we want to wait until we get the right data or the test times out
        assertTrue(rpsRoundTestObserver.test(
                lookupRoundLiveData,
                data -> data != null && data.size() == 1,
                handler));
    }

    /**
     * test that inserting another round for this user works
     * and that fetching after inserting for another user works
     */
    @Test
    public void testRpsRounds() throws Exception {
        final CountDownLatch latch = new CountDownLatch(2);
        repository.insertRound(testRound1);
        repository.insertRound(testRound2);

        LiveData<ArrayList<RpsRound>> lookupRoundLiveData = repository.getAllUserStatsIdRounds(1);
        LiveDataOnChangedHandler<ArrayList<RpsRound>> handler = data -> {
            assertNotNull(data);
            assertEquals(2, data.size());
        };
        assertTrue(rpsRoundTestObserver.test(
                lookupRoundLiveData,
                data -> data != null && data.size() == 2,
                handler));
    }

    /**
     * test that inserting another round for this user works after inserting for another user
     */
    @Test
    public void testRpsRoundsOtherUser() throws Exception {
        repository.insertRound(testRound1);
        repository.insertRound(testRound2);
        repository.insertRound(testRound3);
        LiveData<ArrayList<RpsRound>> lookupRoundLiveData = repository.getAllUserStatsIdRounds(1);
        LiveDataOnChangedHandler<ArrayList<RpsRound>> handler = data -> {
            assertNotNull(data);
            assertEquals(2, data.size());
        };
        assertTrue(rpsRoundTestObserver.test(
                lookupRoundLiveData,
                data -> data != null && data.size() == 2,
                handler));
    }
}

