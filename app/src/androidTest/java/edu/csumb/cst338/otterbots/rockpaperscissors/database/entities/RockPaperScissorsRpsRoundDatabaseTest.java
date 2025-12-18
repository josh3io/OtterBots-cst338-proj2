package edu.csumb.cst338.otterbots.rockpaperscissors.database.entities;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import android.app.Application;
import android.content.Context;
import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.LiveData;
import androidx.test.core.app.ApplicationProvider;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeoutException;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;


/**
 * Run the db tests for RpsRound Author: Josh Goldberg Since: 2025.12.06
 */
@RunWith(androidx.test.ext.junit.runners.AndroidJUnit4.class)
public class RockPaperScissorsRpsRoundDatabaseTest {

  @Rule
  public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();
  TestLiveDataObserver<ArrayList<RpsRound>> rpsRoundTestObserver;
  RpsRound testRound1;
  RpsRound testRound2;
  RpsRound testRound3;
  private TestRepository repository;

  @Before
  public void beforeEach() {
    Context context = ApplicationProvider.getApplicationContext();
    repository = new TestRepository((Application) context);

    assertNotNull(repository);

    // create an observer for livedata tests. see the helper class below.
    rpsRoundTestObserver = new TestLiveDataObserver<>();

    testRound1 = new RpsRound(1, 2, 3, "win");
    testRound2 = new RpsRound(1, 3, 3, "tie");
    testRound3 = new RpsRound(2, 1, 2, "win");
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
    repository.rpsRoundDAO.insert(testRound1);

    // get the data
    LiveData<ArrayList<RpsRound>> lookupRoundLiveData = repository.getAllUserStatsIdRounds(1);

    try {
      ArrayList<RpsRound> lookupRounds = new TestLiveDataObserver<ArrayList<RpsRound>>()
          .getOrAwaitValue(lookupRoundLiveData, data -> data != null && data.size() == 1);
      assertNotNull(lookupRounds);
      assertEquals(1, lookupRounds.size());
      assertEquals("win", lookupRounds.get(0).getResult());
    } catch (TimeoutException e) {
      fail();
    }

    repository.rpsRoundDAO.insert(testRound2);

    try {
      ArrayList<RpsRound> lookupRounds = new TestLiveDataObserver<ArrayList<RpsRound>>()
          .getOrAwaitValue(lookupRoundLiveData, data -> data != null && data.size() == 2);
      assertNotNull(lookupRounds);
      assertEquals(2, lookupRounds.size());
    } catch (TimeoutException e) {
      fail();
    }

  }

  @Test
  public void testDaoUpdate() throws Exception {
    repository.rpsRoundDAO.insert(testRound1);

    // get the data
    LiveData<ArrayList<RpsRound>> lookupRoundLiveData = repository.getAllUserStatsIdRounds(1);

    try {
      ArrayList<RpsRound> lookupRounds = new TestLiveDataObserver<ArrayList<RpsRound>>()
          .getOrAwaitValue(lookupRoundLiveData, data -> data != null && data.size() == 1);
      assertNotNull(lookupRounds);
      assertEquals(1, lookupRounds.size());
      assertEquals("win", lookupRounds.get(0).getResult());
    } catch (TimeoutException e) {
      fail();
    }

    // modify the round
    RpsRound roundToUpdate = lookupRoundLiveData.getValue().get(0);
    roundToUpdate.setResult("FOO");
    repository.rpsRoundDAO.update(roundToUpdate);

    lookupRoundLiveData = repository.getAllUserStatsIdRounds(1);

    try {
      ArrayList<RpsRound> lookupRounds = new TestLiveDataObserver<ArrayList<RpsRound>>()
          .getOrAwaitValue(lookupRoundLiveData, data -> data != null && data.size() == 1);
      assertNotNull(lookupRounds);
      assertEquals(1, lookupRounds.size());
      assertEquals("FOO", lookupRounds.get(0).getResult());
    } catch (TimeoutException e) {
      fail();
    }
  }

  @Test
  public void testDaoDelete() throws Exception {
    repository.rpsRoundDAO.insert(testRound1);

    // get the data
    LiveData<ArrayList<RpsRound>> lookupRoundLiveData = repository.getAllUserStatsIdRounds(1);

    try {
      ArrayList<RpsRound> lookupRounds = new TestLiveDataObserver<ArrayList<RpsRound>>()
          .getOrAwaitValue(lookupRoundLiveData, data -> data != null && data.size() == 1);
      assertNotNull(lookupRounds);
      assertEquals(1, lookupRounds.size());
      assertEquals("win", lookupRounds.get(0).getResult());
    } catch (TimeoutException e) {
      fail();
    }

    // modify the round
    RpsRound roundToUpdate = lookupRoundLiveData.getValue().get(0);
    repository.rpsRoundDAO.delete(roundToUpdate);

    lookupRoundLiveData = repository.getAllUserStatsIdRounds(1);

    try {
      ArrayList<RpsRound> lookupRounds = new TestLiveDataObserver<ArrayList<RpsRound>>()
          .getOrAwaitValue(lookupRoundLiveData, data -> data != null && data.size() == 1);
      fail();
    } catch (TimeoutException e) {
      assertTrue(true);
    }
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
  public void testRpsRound() throws Exception {
    repository.insertRound(testRound1);

    // get the data
    LiveData<ArrayList<RpsRound>> lookupRoundLiveData = repository.getAllUserStatsIdRounds(1);

    try {
      ArrayList<RpsRound> lookupRounds = new TestLiveDataObserver<ArrayList<RpsRound>>()
          .getOrAwaitValue(lookupRoundLiveData, data -> data != null && data.size() == 1);
      assertNotNull(lookupRounds);
      assertEquals(1, lookupRounds.size());
      assertEquals("win", lookupRounds.get(0).getResult());
    } catch (TimeoutException e) {
      fail();
    }
  }

  /**
   * test that inserting another round for this user works and that fetching after inserting for
   * another user works
   */
  @Test
  public void testRpsRounds() throws Exception {
    final CountDownLatch latch = new CountDownLatch(2);
    repository.insertRound(testRound1);
    repository.insertRound(testRound2);

    LiveData<ArrayList<RpsRound>> lookupRoundLiveData = repository.getAllUserStatsIdRounds(1);
    try {
      ArrayList<RpsRound> lookupRounds = new TestLiveDataObserver<ArrayList<RpsRound>>()
          .getOrAwaitValue(lookupRoundLiveData, data -> data != null && data.size() == 2);
      assertNotNull(lookupRounds);
      assertEquals(2, lookupRounds.size());
    } catch (TimeoutException e) {
      fail();
    }
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
    try {
      ArrayList<RpsRound> lookupRounds = new TestLiveDataObserver<ArrayList<RpsRound>>()
          .getOrAwaitValue(lookupRoundLiveData, data -> data != null && data.size() == 2);
      assertNotNull(lookupRounds);
      assertEquals(2, lookupRounds.size());
    } catch (TimeoutException e) {
      fail();
    }

  }

}

