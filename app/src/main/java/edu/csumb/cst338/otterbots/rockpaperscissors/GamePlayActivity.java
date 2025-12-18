package edu.csumb.cst338.otterbots.rockpaperscissors;

import static edu.csumb.cst338.otterbots.rockpaperscissors.LandingActivity.EXTRA_USER_ID;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.HashMap;

import edu.csumb.cst338.otterbots.rockpaperscissors.api.RpsRandomNumberGenerator;
import edu.csumb.cst338.otterbots.rockpaperscissors.database.entities.RockPaperScissorsRepository;
import edu.csumb.cst338.otterbots.rockpaperscissors.database.entities.RpsRound;
import edu.csumb.cst338.otterbots.rockpaperscissors.database.entities.UserStats;
import edu.csumb.cst338.otterbots.rockpaperscissors.databinding.ActivityGamePlayBinding;
import edu.csumb.cst338.otterbots.rockpaperscissors.viewHolders.gameHistory.GameHistoryAdapter;
import edu.csumb.cst338.otterbots.rockpaperscissors.viewHolders.gameHistory.GameHistoryViewModel;

/**
 * GamePlayActivity
 * ----------------
 * Handles a single-player Rock–Paper–Scissors game round for a specific user.
 *
 * Flow per round:
 *  1. User taps ROCK / PAPER / SCISSORS
 *  2. NPC makes a random choice
 *  3. Outcome (win/lose/tie) is computed
 *  4. UI shows choices + result
 *  5. Round + updated UserStats are saved to the database
 */
public class GamePlayActivity extends AppCompatActivity {

  private ActivityGamePlayBinding binding;

  // Maps integer random values to gameplay choices (0=ROCK, 1=PAPER, 2=SCISSORS)
  private final HashMap<Integer, String> GAME_CHOICES = new HashMap<>();

  private String npcCurrentGuess = "";
  private String userCurrentGuess = "";

  // Outcome flags for UI updates
  private boolean userWon;
  private boolean npcWon;
  private boolean roundTie;

  // Player ID for which this gameplay is occurring
  private int userId;

    public static HashMap<Integer,String> makeGameChoices() {
        HashMap<Integer,String> choices = new HashMap<>();
        choices.put(0, "ROCK");
        choices.put(1, "PAPER");
        choices.put(2, "SCISSORS");
        return choices;
    }

  /**
   * Factory method for creating an Intent that launches gameplay for the given user ID.
   */
  static Intent gamePlayActivityIntentFactory(Context context, int userId) {
    Intent intent = new Intent(context, GamePlayActivity.class);
    Log.d(LandingActivity.TAG, "Creating gameplay intent for user " + userId);
    intent.putExtra(EXTRA_USER_ID, userId);
    return intent;
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    // Initialize the game choice map
        GAME_CHOICES = makeGameChoices();

    // Retrieve the user ID passed from the previous activity
    Intent intent = getIntent();
    userId = intent.getIntExtra(EXTRA_USER_ID, -1);

        Log.d(LandingActivity.TAG, "Created gameplay activity for user " + userId);

    // Bind UI layout
    binding = ActivityGamePlayBinding.inflate(getLayoutInflater());
    setContentView(binding.getRoot());

    setupUserUI();
  }

  /**
   * Sets up all button listeners for gameplay.
   * Each button triggers a new round with the selected user choice.
   */
  private void setupUserUI() {
    binding.rockPlayButton.setOnClickListener(
        v -> gamePlayButtonClickHandler(getString(R.string.rock_uppercase)));

    binding.paperPlayButton.setOnClickListener(
        v -> gamePlayButtonClickHandler(getString(R.string.paper_uppercase)));

    binding.scissorsPlayButton.setOnClickListener(
        v -> gamePlayButtonClickHandler(getString(R.string.scissors_uppercase)));

    // Return to previous activity
    binding.returnSelectableTextView.setOnClickListener(v -> finish());
;
        loadHistory();
    }

    private void loadHistory() {
        Log.d(MainActivity.TAG, "Loading History, id "+userId);

        RecyclerView recyclerView = binding.gameHistoryRecyclerView;
        final GameHistoryAdapter adapter = new GameHistoryAdapter(new GameHistoryAdapter.GameHistoryDiff());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        GameHistoryViewModel gameHistoryViewModel = new ViewModelProvider(this).get(GameHistoryViewModel.class);
        gameHistoryViewModel.getLatestRoundsByUser(userId).observe(this, rounds -> {
            adapter.submitList(rounds, () -> {
                recyclerView.smoothScrollToPosition(0);
            });
        });
    }


  /**
   * Main handler for each gameplay round.
   * Steps:
   *  1. NPC chooses a random option
   *  2. User choice is set
   *  3. Determine win/lose/tie
   *  4. Update UI
   *  5. Save round + updated stats to the database
   */
  private void gamePlayButtonClickHandler(String choice) {
    setNpcChoice();
    setUserChoice(choice);
    int outcome = determineOutcome();
    updateGameplayUI();
    updateDatabaseStats(outcome);
  }

  /**
   * Converts a game choice string into its index (0/1/2) for DB storage.
   */
  private int convertChoiceToIndex(String choice) {
    switch (choice) {
      case "ROCK":
        return 0;
      case "PAPER":
        return 1;
      case "SCISSORS":
        return 2;
      default:
        return -1;
    }
  }

  /**
   * Converts an outcome constant to a readable string for DB storage.
   */
  private String outcomeToString(int outcome) {
    switch (outcome) {
      case UserStats.WIN:
        return "WIN";
      case UserStats.TIE:
        return "TIE";
      case UserStats.LOSE:
        return "LOSE";
      default:
        return "UNKNOWN";
    }
  }

  /**
   * Updates the database with:
   *  - A new round entry in the rounds table
   *  - Updated cumulative user statistics (wins, losses, ties, streak)
   *
   * Uses LiveData to retrieve and update user stats, then removes its observer.
   */
  private void updateDatabaseStats(int outcome) {
    Log.d(LandingActivity.TAG, "update db stats with outcome " + outcome);
    RockPaperScissorsRepository repository =
        RockPaperScissorsRepository.getRepository(getApplication());

    if (repository == null) {
      toastMaker("Failed to update the database due to db error.");
      return;
    }

    LiveData<UserStats> userStatsLiveData = repository.getUserStatsByUserId(userId);

    Observer<UserStats> userStatsObserver =
        new Observer<UserStats>() {
          @Override
          public void onChanged(UserStats userStats) {

            // If no stats exist yet, create a blank stats object
            if (userStats == null) {
              userStats = new UserStats(userId, 0, 0, 0, 0, 0);
            }

            // Store the round result in the rounds table
            int userChoiceIndex = convertChoiceToIndex(userCurrentGuess);
            int npcChoiceIndex = convertChoiceToIndex(npcCurrentGuess);
            String resultString = outcomeToString(outcome);

            RpsRound round =
                new RpsRound(userId, userChoiceIndex, npcChoiceIndex, resultString);
            repository.insertRound(round);

            Log.d(LandingActivity.TAG, "Inserted round: " + round);

            // Update cumulative stats based on outcome
            switch (outcome) {
              case UserStats.WIN:
                userStats.setCurrentStreak(userStats.getCurrentStreak() + 1);
                if (userStats.getCurrentStreak() > userStats.getMaxStreak()) {
                  userStats.setMaxStreak(userStats.getCurrentStreak());
                }
                userStats.setWins(userStats.getWins() + 1);
                break;
              case UserStats.TIE:
                userStats.setCurrentStreak(0);
                userStats.setTies(userStats.getTies() + 1);
                break;
              case UserStats.LOSE:
                userStats.setCurrentStreak(0);
                userStats.setLosses(userStats.getLosses() + 1);
                break;
              default:
                Log.e(LandingActivity.TAG, "Unhandled outcome option: " + outcome);
            }

            Log.d(LandingActivity.TAG, "Updating user stats " + userStats);
            repository.insertOrUpdateUserStats(userStats);

            // Remove observer to ensure update runs only once per round
            userStatsLiveData.removeObserver(this);
          }
        };

    // observeForever() ensures callback runs even if lifecycle state changes briefly
    userStatsLiveData.observeForever(userStatsObserver);
  }

  /** Sets the user's current choice. */
  private void setUserChoice(String userChoice) {
    userCurrentGuess = userChoice;
  }

  /** Randomly selects NPC choice using helper class. */
  private void setNpcChoice() {
    int npcGuess = RpsRandomNumberGenerator.getRandomNumber();
    npcCurrentGuess = GAME_CHOICES.get(npcGuess);
  }

  /**
   * Determines win/lose/tie result according to Rock–Paper–Scissors rules.
   * Sets internal boolean flags used by UI update method.
   */
  private int determineOutcome() {

    // User winning scenarios
    if (userCurrentGuess.equals(GAME_CHOICES.get(0)) && npcCurrentGuess.equals(GAME_CHOICES.get(2))
        || userCurrentGuess.equals(GAME_CHOICES.get(1))
            && npcCurrentGuess.equals(GAME_CHOICES.get(0))
        || userCurrentGuess.equals(GAME_CHOICES.get(2))
            && npcCurrentGuess.equals(GAME_CHOICES.get(1))) {

      roundTie = false;
      userWon = true;
      npcWon = false;
      return UserStats.WIN;
    }

    // Tie scenario
    if (userCurrentGuess.equals(npcCurrentGuess)) {
      roundTie = true;
      userWon = false;
      npcWon = false;
      return UserStats.TIE;
    }

    // Otherwise NPC wins
    roundTie = false;
    userWon = false;
    npcWon = true;
    return UserStats.LOSE;
  }

  /**
   * Updates UI labels showing:
   *  - User choice
   *  - NPC choice
   *  - Result message (win/lose/tie)
   */
  private void updateGameplayUI() {
    binding.youChoseOutputTextView.setText(userCurrentGuess);
    binding.npcChoseOutputTextView.setText(npcCurrentGuess);

    if (userWon) {
      binding.resultOutputTextView.setText(R.string.you_win);
    } else if (npcWon) {
      binding.resultOutputTextView.setText(R.string.you_lose);
    } else if (roundTie) {
      binding.resultOutputTextView.setText(R.string.it_s_a_tie);
    }
  }

  /** Helper method for showing simple Toast messages. */
  private void toastMaker(String message) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
  }
}