package edu.csumb.cst338.otterbots.rockpaperscissors.api;

import android.util.Log;
import edu.csumb.cst338.otterbots.rockpaperscissors.MainActivity;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Description: Call an API to get a truly random number, not pseudorandom get 100 at a time and
 * store locally. when the list gets low, get more. Author: Josh Goldberg Since: 2025.12.08
 */
public class RpsRandomNumberGenerator {

  // with RPS there are 3 choices, so we want random nubmers 0 - 2 inclusive
  private static final int MIN_CHOICE_INT = 0;
  private static final int MAX_CHOICE_INT = 2;

  // prefetch 100 random numbers at a time
  private static final int RANDOM_NUMBERS_TO_FETCH = 100;

  // when we have 10 or fewer remaining, get more!
  private static final int THRESHOLD_REMAINING_TO_PREFETCH_MORE = 10;
  private static final String API_URL = "https://random.org/integers/?"
      + "num=" + RANDOM_NUMBERS_TO_FETCH
      + "&min=" + MIN_CHOICE_INT
      + "&max=" + MAX_CHOICE_INT
      + "&col=1&base=10&format=plain&rnd=new";

  // run requests in a separate single thread so we don't DoS the API
  private static final ExecutorService executorService = Executors.newSingleThreadExecutor();
  private static RpsRandomNumberGenerator INSTANCE;
  private final ArrayList<Integer> randomNumbers;

  /**
   * Private singleton constructor
   */
  private RpsRandomNumberGenerator() {
    randomNumbers = new ArrayList<>();
    getMoreNumbers();
  }

  /**
   * Initialize the singleton without returning a random number
   */
  public static void initializeGenerator() {
    if (INSTANCE == null) {
      // new singleton
      INSTANCE = new RpsRandomNumberGenerator();
    }
  }

  /**
   * Get a new truly random number. if none available, fallback to pseudo-random
   *
   * @return a random integer
   */
  public static int getRandomNumber() {
    initializeGenerator();
    // only get more from the API if we're running low
    if (INSTANCE.randomNumbers.size() < THRESHOLD_REMAINING_TO_PREFETCH_MORE) {
      INSTANCE.getMoreNumbers();
    }
    if (!INSTANCE.randomNumbers.isEmpty()) {
      return INSTANCE.randomNumbers.remove(0);
    } else {
      // fire off a thread to get more numbers
      // fallback
      Random r = new Random();
      return r.nextInt(3);
    }
  }


  /**
   * Call the API to get more numbers to store in the singleton. the API provider likes us to make
   * batch calls.
   */
  private void getMoreNumbers() {
    executorService.execute(() -> {
      try {
        // only get more from the API if we're running low
        if (INSTANCE.randomNumbers.size() > THRESHOLD_REMAINING_TO_PREFETCH_MORE) {
          return;
        }
        URL url = new URL(API_URL);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setConnectTimeout(5000);
        connection.setReadTimeout(5000);
        connection.connect();
        int responseCode = connection.getResponseCode();

        if (responseCode == HttpURLConnection.HTTP_OK) {
          BufferedReader in = new BufferedReader(
              (new InputStreamReader(connection.getInputStream())));
          String inputLine;
          while ((inputLine = in.readLine()) != null) {
            int number = Integer.parseInt(inputLine);
            Log.d(MainActivity.TAG, "Adding number " + number);
            INSTANCE.randomNumbers.add(number);
          }
          in.close();
        } else {
          Log.e(MainActivity.TAG, "Random.org random number generation API call failed");
        }
      } catch (Exception e) {
        Log.e(MainActivity.TAG,
            "Random.org random number generation API call failed with exception " + e);
      }
    });
  }
}
