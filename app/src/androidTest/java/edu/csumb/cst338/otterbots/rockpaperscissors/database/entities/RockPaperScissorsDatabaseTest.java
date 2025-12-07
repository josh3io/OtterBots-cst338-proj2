package edu.csumb.cst338.otterbots.rockpaperscissors.database.entities;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import android.app.Application;
import android.content.Context;

import androidx.arch.core.executor.ArchTaskExecutor;
import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
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
import java.util.concurrent.TimeUnit;

@RunWith(androidx.test.ext.junit.runners.AndroidJUnit4.class)
public class RockPaperScissorsDatabaseTest {
    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    private RockPaperScissorsDatabase db;

    private TestRepository repository;

    private Context context;

    LiveDataObserver<ArrayList<RpsRound>> rpsRoundTestObserver;

    RpsRound testRound1;
    RpsRound testRound2;
    RpsRound testRound3;


    /**
     * extend the RPS repository so we can mock with an in memory database
     */
    private class TestRepository extends RockPaperScissorsRepository {
        public TestRepository(Application application) {
            db = Room.inMemoryDatabaseBuilder(application, RockPaperScissorsDatabase.class)
                    .setTransactionExecutor(Executors.newSingleThreadExecutor())
                    .allowMainThreadQueries()
                    .build();
            this.rpsRoundDAO = db.rpsRoundDAO();
            this.userStatsDAO = db.userStatsDAO();
        }
    }

    @Before
    public void beforeEach() {
        context = ApplicationProvider.getApplicationContext();
        repository = new TestRepository((Application) context);

        assertNotNull(repository);

        rpsRoundTestObserver = new LiveDataObserver<>();

        testRound1 = new RpsRound(1,2,3,"win");
        testRound2 = new RpsRound(1,3,3,"tie");
        testRound3 = new RpsRound(2,1,2,"win");
    }

    @After
    public void afterEach() throws IOException {
        db.close();
        repository = null;
    }

    @Test
    public void testGetDatabase() throws Exception{
        System.out.println("Testing database creation and factory");
        assert(db != null);
    }

    //TODO: test access here
    /**
     * test a single record insert
     */
    @Test
    public void testRpsRound() throws Exception {
        repository.insertRound(testRound1);
        LiveData<ArrayList<RpsRound>> lookupRoundLiveData = repository.getAllUserStatsIdRounds(1);

        OnChangedHandler<ArrayList<RpsRound>> handler = data -> {
            assertNotNull(data);
            assertEquals(1, data.size());
            assertEquals("win", data.get(0).getResult());
        };
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
        OnChangedHandler<ArrayList<RpsRound>> handler = data -> {
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
        OnChangedHandler<ArrayList<RpsRound>> handler = data -> {
            assertNotNull(data);
            assertEquals(2, data.size());
        };
        assertTrue(rpsRoundTestObserver.test(
                lookupRoundLiveData,
                data -> data != null && data.size() == 2,
                handler));
    }
}

@FunctionalInterface
interface OnChangedHandler<T> {
    void handle(T data);
}

class LiveDataObserver<T> {
    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();
    public boolean test(LiveData<T> liveData, java.util.function.Predicate<T> condition, OnChangedHandler<T> handler) throws InterruptedException {
        final CountDownLatch latch = new CountDownLatch(1);

        Observer<T> observer = new Observer<T>() {
            @Override
            public void onChanged(T data) {
                if (!condition.test(data)) {
                    return;
                }
                handler.handle(data);
                latch.countDown();

                ArchTaskExecutor.getInstance().executeOnMainThread(
                    () -> liveData.removeObserver(this)
                );
            }
        };
        liveData.observeForever(observer);
        return latch.await(5, TimeUnit.SECONDS);
    }
}