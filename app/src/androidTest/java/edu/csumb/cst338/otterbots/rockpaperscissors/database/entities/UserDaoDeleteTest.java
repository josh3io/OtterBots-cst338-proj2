package edu.csumb.cst338.otterbots.rockpaperscissors.database.entities;

/*
 * Author: Christopher Buenrostro
 * Description:
 *   Instrumented test verifying that deleting a user by username
 *   removes only that specific user from the in-memory Room database.
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
     * Creates a fresh in-memory database before each test.
     */
    @Before
    public void setup() {
        Context context = ApplicationProvider.getApplicationContext();
        db = Room.inMemoryDatabaseBuilder(context, RockPaperScissorsDatabase.class)
                .allowMainThreadQueries() // Safe for testing
                .build();
        userDao = db.userDAO();
    }

    /**
     * Closes the database after each test.
     */
    @After
    public void tearDown() {
        db.close();
    }

    /**
     * Level 1 Test:
     * Ensures deleteUserByUsername removes only the specified user.
     */
    @Test
    public void deleteUserByUsername_removesOnlyThatUser() throws Exception {
        // Insert two test users
        userDao.insert(new User("user1", "pw1", 0));
        userDao.insert(new User("user2", "pw2", 1));

        // Sanity check: two users should exist
        List<User> initial = getOrAwaitValue(userDao.getAllUsers());
        assertEquals(2, initial.size());

        // Delete user1
        userDao.deleteUserByUsername("user1");

        // Verify only user2 remains
        List<User> remaining = getOrAwaitValue(userDao.getAllUsers());
        assertEquals(1, remaining.size());
        assertEquals("user2", remaining.get(0).getUsername());
    }

    /**
     * Utility method:
     * Retrieves a value from LiveData synchronously for testing.
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
