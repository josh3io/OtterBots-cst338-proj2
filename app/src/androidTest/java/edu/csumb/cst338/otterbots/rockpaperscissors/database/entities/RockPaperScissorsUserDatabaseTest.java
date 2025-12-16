package edu.csumb.cst338.otterbots.rockpaperscissors.database.entities;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

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
import java.util.concurrent.TimeoutException;

/**
 * Run the db tests for UserStats
 * Author: Josh Goldberg
 * Since: 2025.12.06
 */
@RunWith(androidx.test.ext.junit.runners.AndroidJUnit4.class)
public class RockPaperScissorsUserDatabaseTest {
    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    private TestRepository repository;

    User testUser1;
    User testUser2;

    @Before
    public void beforeEach() {
        Context context = ApplicationProvider.getApplicationContext();
        repository = new TestRepository((Application) context);

        assertNotNull(repository);

        testUser1 = new User("user1", "pass1", 0);
        testUser2 = new User("user2", "pass2", 0);

        repository.insertUser(testUser1);
        repository.insertUser(testUser2);
    }

    @After
    public void afterEach() throws IOException {
        repository = null;
    }

    @Test
    public void testUserInsertion() throws Exception {
        // make sure the user record is there
        LiveData<User> user1LiveData = repository.getUserByUsername("user1");
        try {
            User user = new TestLiveDataObserver<User>()
                    .getOrAwaitValue(user1LiveData,Objects::nonNull);
            assertNotNull(user);
            assertEquals(1, user.getUserId());
        } catch (TimeoutException e) {
            fail();
        }
    }
}
