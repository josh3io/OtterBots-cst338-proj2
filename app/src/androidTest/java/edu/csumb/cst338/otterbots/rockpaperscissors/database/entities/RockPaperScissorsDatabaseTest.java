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
     * and a single thread executor and allow main thread queries
     */
    private class TestRepository extends RockPaperScissorsRepository {
        public TestRepository(Application application) {
            // get an in memory db instead of our singleton
            db = Room.inMemoryDatabaseBuilder(application, RockPaperScissorsDatabase.class)
                    .setTransactionExecutor(Executors.newSingleThreadExecutor())
                    .allowMainThreadQueries()
                    .build();

            // and load up the DAOs here.
            this.rpsRoundDAO = db.rpsRoundDAO();
            this.userStatsDAO = db.userStatsDAO();
        }
    }

    @Before
    public void beforeEach() {
        context = ApplicationProvider.getApplicationContext();
        repository = new TestRepository((Application) context);

        assertNotNull(repository);

        // create an observer for livedata tests. see the helper class below.
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

        // get the data
        LiveData<ArrayList<RpsRound>> lookupRoundLiveData = repository.getAllUserStatsIdRounds(1);

        // this handler for the livedata observer runs our tests
        OnChangedHandler<ArrayList<RpsRound>> handler = data -> {
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


/**
 * Handler function template for LiveData Observer
 * @param <T> the type we will be operating on.
 */
@FunctionalInterface
interface OnChangedHandler<T> {
    /**
     * Handle the onChange for livedata observer
     * @param data the data we will handle.
     */
    void handle(T data);
}

/**
 * Template class to observe live data change results
 * @param <T> the type of data we will be acting on in our change handler
 */
class LiveDataObserver<T> {
    /**
     * This is where we will run our tests or timeout trying.
     * @param liveData the LiveData<T> we will be acting on
     * @param condition Wait for this condition to be true to run our change handler. this protects against early intermediate changes
     * @param handler This is what will run on our LiveData wrapped data. we want to do our data testing here.
     * @return boolean if the observer times out. this will happen if our 'condition' is not met.
     * @throws InterruptedException if something interrupts the processing
     */
    public boolean test(LiveData<T> liveData, java.util.function.Predicate<T> condition, OnChangedHandler<T> handler) throws InterruptedException {
        // set a latch that we can decrement once our tests are done
        final CountDownLatch latch = new CountDownLatch(1);

        // the actual observer
        Observer<T> observer = new Observer<T>() {
            @Override
            public void onChanged(T data) {
                // don't act on our data too early! wait for preconditions
                if (!condition.test(data)) {
                    return;
                }
                // handler will run the actual tests we are interested in
                handler.handle(data);
                // now that we are done, decrement the latch
                latch.countDown();

                // without this, removeObserver happens in a background thread and that's broken.
                // wrap it so it runs on the main thread.
                ArchTaskExecutor.getInstance().executeOnMainThread(
                    () -> liveData.removeObserver(this)
                );
            }
        };
        // run the observer
        liveData.observeForever(observer);
        // wait for the latch to decrement for up to 5 seconds. Tests should handle the return value
        // or we could end up with false positives for tests.
        return latch.await(5, TimeUnit.SECONDS);
    }
}