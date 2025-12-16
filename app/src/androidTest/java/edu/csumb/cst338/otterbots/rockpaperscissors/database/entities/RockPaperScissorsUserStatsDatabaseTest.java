package edu.csumb.cst338.otterbots.rockpaperscissors.database.entities;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import android.app.Application;
import android.content.Context;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;
import androidx.test.core.app.ApplicationProvider;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeoutException;
import java.util.function.BiFunction;
import java.util.function.Function;

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

    TestLiveDataObserver<ArrayList<UserJoinUserStats>> userJoinUserStatsTestLiveDataObserver;

    UserStats testStats1;
    UserStats testStats2;
    UserStats testStats3;
    User testUser1;
    User testUser2;

    @Before
    public void beforeEach() {
        Context context = ApplicationProvider.getApplicationContext();
        repository = new TestRepository((Application) context);

        assertNotNull(repository);

        // create an observer for livedata tests. see the helper class below.
        listUserStatsTestObserver = new TestLiveDataObserver<>();
        singleUserStatsTestObserver = new TestLiveDataObserver<>();
        userJoinUserStatsTestLiveDataObserver = new TestLiveDataObserver<>();

        testUser1 = new User("user1", "pass1", 0);
        testUser2 = new User("user2", "pass2", 0);

        repository.insertUser(testUser1);
        repository.insertUser(testUser2);

        testStats1 = new UserStats(1, 2, 3, 4, 5, 5);
        testStats2 = new UserStats(1, 3, 3, 4, 5, 5);
        testStats3 = new UserStats(2, 2, 3, 4, 5, 5);
    }

    @After
    public void afterEach() throws IOException {
        repository = null;
    }

    /*
     * *********************
     * BARE TESTS FOR DAO
     * *********************
     */
    @Test
    public void testDaoInsert() throws Exception {
        repository.userStatsDAO.insert(testStats1);

        // get the data
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
    public void testDaoUpdate() throws Exception {
        repository.userStatsDAO.insert(testStats1);

        // get the data
        LiveData<UserStats> userStatsLiveData = repository.getUserStatsByUserId(1);

        // this handler for the livedata observer runs our tests
        LiveDataOnChangedHandler<UserStats> handler = Assert::assertNotNull;
        // we want to wait until we get the right data or the test times out
        assertTrue(singleUserStatsTestObserver.test(
                userStatsLiveData,
                Objects::nonNull,
                handler));

        // modify the stats
        UserStats stats = userStatsLiveData.getValue();
        stats.setWins(100);
        repository.userStatsDAO.update(stats);

        userStatsLiveData = repository.getUserStatsByUserId(1);

        // this handler for the livedata observer runs our tests
        // the wins should have changed
        handler = data -> {
            assertNotNull(data);
            assertEquals(100, stats.getWins());
        };
        // we want to wait until we get the right data or the test times out
        assertTrue(singleUserStatsTestObserver.test(
                userStatsLiveData,
                Objects::nonNull,
                handler));
    }

    @Test
    public void testDaoDelete() throws Exception {
        repository.userStatsDAO.insert(testStats1);

        // get the data
        LiveData<UserStats> userStatsLiveData = repository.getUserStatsByUserId(1);

        // this handler for the livedata observer runs our tests
        LiveDataOnChangedHandler<UserStats> handler = Assert::assertNotNull;
        // we want to wait until we get the right data or the test times out
        assertTrue(singleUserStatsTestObserver.test(
                userStatsLiveData,
                Objects::nonNull,
                handler));

        // delete the round
        repository.userStatsDAO.delete(userStatsLiveData.getValue());

        userStatsLiveData = repository.getUserStatsByUserId(1);

        // this handler for the livedata observer runs our tests
        // the size shouldn't change, but 0th entry should have changed
        handler = data -> {
            // this should never run
            fail();
        };
        // we want to wait until we get the right data or the test times out
        // this should timeout because after delete, there should be no records
        // so the predicate fails
        assertFalse(singleUserStatsTestObserver.test(
                userStatsLiveData,
                Objects::nonNull,
                handler));
    }

    /*
     * **********************
     * TEST REPOSITORY ACCESS
     * **********************
     */


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

    /**
     * Test a second insert for the same user. it should replace.
     */
    @Test
    public void testUpdate() throws Exception {
        LiveData<ArrayList<UserJoinUserStats>> initialLeaderboardDataLiveData = repository.getAllUserStatsByRank();
        try {
            ArrayList<UserJoinUserStats> initialLeaderboardData = new TestLiveDataObserver<ArrayList<UserJoinUserStats>>()
                    .getOrAwaitValue(initialLeaderboardDataLiveData, Objects::nonNull, 2);
            assertNull(initialLeaderboardData.get(0).getUserStats());
            assertNull(initialLeaderboardData.get(1).getUserStats());
        } catch (TimeoutException e) {
            fail();
        }


        repository.insertOrUpdateUserStats(testStats1);

        LiveData<UserStats> userStatsLiveData = repository.getUserStatsByUserId(1);
        UserStats newUserStatsData = singleUserStatsTestObserver.getOrAwaitValue(userStatsLiveData,Objects::nonNull, 2);

        assertNotNull(newUserStatsData);

        assertEquals(1, newUserStatsData.getUserId());
        // this is from the first query
        assertEquals(2, newUserStatsData.getWins());

        // update a stat
        newUserStatsData.setWins(newUserStatsData.getWins() + 1);

        // do the update
        repository.insertOrUpdateUserStats(newUserStatsData);

        // get the updated data
        LiveData<UserStats> userStatsLiveData2 = repository.getUserStatsByUserId(1);
        UserStats newUserStatsData2 = singleUserStatsTestObserver.getOrAwaitValue(userStatsLiveData2,Objects::nonNull, 2);

        // test the update
        assertEquals(1, newUserStatsData2.getUserId());
        // this is from the update query
        assertEquals(3, newUserStatsData2.getWins());


        // test that we still only have one record
        LiveData<ArrayList<UserJoinUserStats>> allUserStatsLiveData = repository.getAllUserStatsByRank();
        // observer the list query to be sure we only get two records back
        // waiting for three records should timeout
        // because this should be an update
        LiveData<ArrayList<UserJoinUserStats>> leaderboardDataLiveData = repository.getAllUserStatsByRank();
        ArrayList<UserJoinUserStats> leaderboardData;
        try {
            leaderboardData = new TestLiveDataObserver<ArrayList<UserJoinUserStats>>().getOrAwaitValue(
                    leaderboardDataLiveData,
                    predicateData -> {
                        return predicateData.size() > 2;
                    },
                    2
            );
            // the await should timeout because the predicate should never be true since we are updating, not adding new stats record
            fail();
        } catch (TimeoutException e) {
            assertTrue(true);
        }

        leaderboardData = new TestLiveDataObserver<ArrayList<UserJoinUserStats>>().getOrAwaitValue(
                leaderboardDataLiveData,
                predicateData -> {
                    return predicateData.size() == 2;
                },
                2
        );
        assertEquals(3, leaderboardData.get(0).getUserStats().getWins()); //user 1 stats are updated here
        assertNull(leaderboardData.get(1).getUserStats()); //user 2 stats are still null

    }

    /**
     * Test the different sorting critera of the leaderboard
     */
    @Test
    public void testLeaderboardSorting() throws Exception {
        // most wins first
        repository.insertOrUpdateUserStats(
                new UserStats(1,5,4,3,2,1)
        );
        repository.insertOrUpdateUserStats(
                new UserStats(2,4,4,3,0,0)
        );

        BiFunction<LiveData<ArrayList<UserJoinUserStats>>, List<Integer>, Boolean> leaderBoardTest
                = (LiveData<ArrayList<UserJoinUserStats>> liveData, List<Integer> expectedUserIdOrder) -> {
            try {
                ArrayList<UserJoinUserStats> leaderboardData
                        = new TestLiveDataObserver<ArrayList<UserJoinUserStats>>()
                            .getOrAwaitValue(liveData,Objects::nonNull, 2);
                assertNotNull(leaderboardData);
                for (int i=0;i<expectedUserIdOrder.size();i++) {
                    assertEquals((int) expectedUserIdOrder.get(i), leaderboardData.get(i).getUserId());
                }
                return true;
            } catch (TimeoutException e) {
                fail();
                return false;
            }
        };

        LiveData<ArrayList<UserJoinUserStats>> leaderboardLiveData = repository.getAllUserStatsByRank();
        assertTrue(leaderBoardTest.apply(leaderboardLiveData, List.of(1,2)));

        // make user 2 have same wins, fewest losses. should become top rank
        repository.insertOrUpdateUserStats(
                new UserStats(2,5,2,3,2,1)
        );
        leaderboardLiveData = repository.getAllUserStatsByRank();
        assertTrue(leaderBoardTest.apply(leaderboardLiveData, List.of(2,1)));

        // when wins and losses are equal, most ties is top rank
        repository.insertOrUpdateUserStats(
                new UserStats(2,5,4,0,0,0)
        );
        leaderboardLiveData = repository.getAllUserStatsByRank();
        assertTrue(leaderBoardTest.apply(leaderboardLiveData, List.of(1,2)));

        // when wins and losses and ties are equal, max streak desc
        repository.insertOrUpdateUserStats(
                new UserStats(2,5,4,3,4,0)
        );
        leaderboardLiveData = repository.getAllUserStatsByRank();
        assertTrue(leaderBoardTest.apply(leaderboardLiveData, List.of(2,1)));

        // when wins and losses and ties and maxStreak are equal, current streak desc
        repository.insertOrUpdateUserStats(
                new UserStats(2,5,4,3,2,0)
        );
        leaderboardLiveData = repository.getAllUserStatsByRank();
        assertTrue(leaderBoardTest.apply(leaderboardLiveData, List.of(1,2)));
    }

    /**
     * Test that we have a leaderboard after two users add stats records
     *
     * @throws Exception
     */
    @Test
    public void testMultipleUsersLeaderboard() throws Exception {

        repository.insertOrUpdateUserStats(testStats1);
        repository.insertOrUpdateUserStats(testStats3);

        // assert that the stats were added
        LiveData<UserStats> userStatsLiveData = repository.getUserStatsByUserId(1);
        LiveDataOnChangedHandler<UserStats> userStatsHandler = data -> {
            assertEquals(1, data.getUserId());
        };
        assertTrue(singleUserStatsTestObserver.test(userStatsLiveData, Objects::nonNull, userStatsHandler));

        userStatsLiveData = repository.getUserStatsByUserId(2);
        userStatsHandler = data -> {
            assertEquals(2, data.getUserId());
        };
        assertTrue(singleUserStatsTestObserver.test(userStatsLiveData, Objects::nonNull, userStatsHandler));

        // test that we still have two records for the two users
        LiveData<ArrayList<UserJoinUserStats>> allUserStatsLiveData = repository.getAllUserStatsByRank();
        // observer the list query to be sure we only get one record back
        // waiting for two records should timeout
        LiveDataOnChangedHandler<ArrayList<UserJoinUserStats>> listHandler = data -> {
            // this should never run
            assertEquals(2, data.size());
        };
        assertTrue(userJoinUserStatsTestLiveDataObserver.test(
                allUserStatsLiveData,
                data -> data.size() > 1,
                listHandler
        ));
    }

    @Test
    public void testUserInsertion() throws Exception {
        // make sure the user record is there
        LiveData<User> user1LiveData = repository.getUserByUsername("user1");
        LiveDataOnChangedHandler<User> userHandler = userData -> {
            assertEquals("user1", userData.getUsername());
        };
        TestLiveDataObserver<User> userTestLiveDataObserver = new TestLiveDataObserver<User>();
        assertTrue(userTestLiveDataObserver.test(
                user1LiveData,
                Objects::nonNull,
                userHandler
        ));
    }
}
