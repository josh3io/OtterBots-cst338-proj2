package edu.csumb.cst338.otterbots.rockpaperscissors.database.entities;

/*
 * Author: Christopher Buenrostro
 * Description:
 *   Instrumented test validating that the UserDAO correctly removes
 *   only the targeted user when deleteUserByUsername() is executed.
 *   This ensures that user deletion is precise and does not affect
 *   other existing records in the Room database.
 */

import static org.junit.Assert.*;

import android.content.Context;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.LiveData;
import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.*;
import org.junit.runner.RunWith;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

@RunWith(AndroidJUnit4.class)
public class UserDaoDeleteTest {

    @Rule
    public InstantTaskExecutorRule rule = new InstantTaskExecutorRule();

    private RockPaperScissorsDatabase db;
    private UserDAO userDao;

    /**
     * Before each test, build a brand-new in-memory Room database.
     * Using an in-memory instance ensures isolation between tests
     * and avoids persisting data across runs. The database allows
     * main-thread queries because this is a controlled test environment.
     */
    @Before
    public void setup() {
        Context context = ApplicationProvider.getApplicationContext();
        db = Room.inMemoryDatabaseBuilder(context, RockPaperScissorsDatabase.class)
                .allowMainThreadQueries() // Safe and convenient for testing
                .build();
        userDao = db.userDAO();
    }

    /**
     * After each test, close the database to release resources and
     * guarantee a clean starting point for the next test method.
     */
    @After
    public void tearDown() {
        db.close();
    }

    /**
     * Level 1 Test Requirement:
     * Verifies that deleteUserByUsername() removes exactly one user—
     * the one matching the provided username—while leaving all other
     * users intact. This test inserts two users, deletes one of them,
     * and then checks that the remaining user is still present.
     */
    @Test
    public void deleteUserByUsername_removesOnlyThatUser() throws Exception {
        // Insert initial test users
        userDao.insert(new User("user1", "pw1", 0));
        userDao.insert(new User("user2", "pw2", 1));

        // Confirm that two users exist before deletion
        List<User> initial = getOrAwaitValue(userDao.getAllUsers());
        assertEquals(2, initial.size());

        // Delete the first user
        userDao.deleteUserByUsername("user1");

        // Verify that only the second user remains
        List<User> remaining = getOrAwaitValue(userDao.getAllUsers());
        assertEquals(1, remaining.size());
        assertEquals("user2", remaining.get(0).getUsername());
    }

    /**
     * Helper utility for retrieving the current value from LiveData
     * in a blocking, synchronous manner. This enables test assertions
     * to operate on LiveData values directly. The method observes
     * LiveData forever, captures the emitted value, and unblocks the
     * test thread once the data is received or the timeout expires.
     */
    private static <T> T getOrAwaitValue(LiveData<T> liveData) throws InterruptedException {
        final Object[] data = new Object[1];
        CountDownLatch latch = new CountDownLatch(1);

        liveData.observeForever(value -> {
            data[0] = value;
            latch.countDown();
        });

        if (!latch.await(2, TimeUnit.SECONDS)) {
            throw new AssertionError("LiveData timed out while waiting for value.");
        }

        // noinspection unchecked
        return (T) data[0];
    }
}
