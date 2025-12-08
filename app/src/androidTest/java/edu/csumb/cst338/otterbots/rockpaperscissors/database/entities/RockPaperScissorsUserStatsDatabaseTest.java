package edu.csumb.cst338.otterbots.rockpaperscissors.database.entities;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import android.app.Application;
import android.content.Context;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.LiveData;
import androidx.test.core.app.ApplicationProvider;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

/**
 * Run the db tests for UserStats
 * Author: Josh Goldberg
 * Since: 2025.12.06
 */
@RunWith(androidx.test.ext.junit.runners.AndroidJUnit4.class)
public class RockPaperScissorsUserStatsDatabaseTest {
    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    private RockPaperScissorsDatabase db;

    private TestRepository repository;

    TestLiveDataObserver<ArrayList<UserStats>> listUserStatsTestObserver;
    TestLiveDataObserver<UserStats> singleUserStatsTestObserver;

    UserStats testStats1;
    UserStats testStats2;
    UserStats testStats3;

    @Before
    public void beforeEach() {
        Context context = ApplicationProvider.getApplicationContext();
        repository = new TestRepository((Application) context);

        assertNotNull(repository);

        // create an observer for livedata tests. see the helper class below.
        listUserStatsTestObserver = new TestLiveDataObserver<>();
        singleUserStatsTestObserver = new TestLiveDataObserver<>();

        testStats1 = new UserStats(1,2,3,4,5,5);
        testStats2 = new UserStats(1,3,3,4,5,5);
        testStats3 = new UserStats(2,2,3,4,5,5);
    }

    @After
    public void afterEach() throws IOException {
        repository = null;
    }
    /**
     * test a single record insert
     */
    @Test
    public void testInsert() throws Exception {
        repository.insertOrUpdateUserStats(testStats1);

        LiveData<UserStats> userStatsLiveData = repository.getUserStatsByUserId(1);

        // this handler for the livedata observer runs our tests
        LiveDataOnChangedHandler<UserStats> handler = data -> {
            assertNotNull(data);
            assertEquals(1, data.getUserId());
        };
        // we want to wait until we get the right data or the test times out
        assertTrue(singleUserStatsTestObserver.test(
                userStatsLiveData,
                Objects::nonNull,
                handler));
    }

    @Test
    public void testUpdate() throws Exception {
        repository.insertOrUpdateUserStats(testStats1);

        LiveData<UserStats> userStatsLiveData = repository.getUserStatsByUserId(1);

        LiveDataOnChangedHandler<UserStats> handler = data -> {
            assertEquals(1, data.getUserId());
            // this is from the first query
            assertEquals(2, data.getWins());

        };
        assertTrue(singleUserStatsTestObserver.test(
                userStatsLiveData,
                Objects::nonNull,
                handler
        ));

        // do the second insert
        repository.insertOrUpdateUserStats(testStats2);
        // query again
        userStatsLiveData = repository.getUserStatsByUserId(1);
        LiveDataOnChangedHandler<UserStats> handler2 = data2 -> {
            assertEquals(1, data2.getUserId());
            // this is from the update query
            assertEquals(3, data2.getWins());
        };
        assertTrue(singleUserStatsTestObserver.test(
                userStatsLiveData,
                Objects::nonNull,
                handler2
        ));

        LiveData<ArrayList<UserStats>> allUserStatsLiveData = repository.getAllUserStatsByRank();
        // observer the list query to be sure we only get one record back
        // waiting for two records should timeout
        LiveDataOnChangedHandler<ArrayList<UserStats>> listHandler = data -> {
            // this should never run
            assertEquals(1, data.size());
        };
        assertFalse(listUserStatsTestObserver.test(
                allUserStatsLiveData,
                data -> data.size() > 1,
                listHandler
        ));
    }
}
