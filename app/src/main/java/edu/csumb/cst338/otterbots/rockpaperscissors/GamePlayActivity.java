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

import java.util.HashMap;

import edu.csumb.cst338.otterbots.rockpaperscissors.api.RpsRandomNumberGenerator;
import edu.csumb.cst338.otterbots.rockpaperscissors.database.entities.RockPaperScissorsRepository;
import edu.csumb.cst338.otterbots.rockpaperscissors.database.entities.RpsRound;
import edu.csumb.cst338.otterbots.rockpaperscissors.database.entities.UserStats;
import edu.csumb.cst338.otterbots.rockpaperscissors.databinding.ActivityGamePlayBinding;

public class GamePlayActivity extends AppCompatActivity {

    private ActivityGamePlayBinding binding;
    private HashMap<Integer, String> GAME_CHOICES = new HashMap<>();
    private String npcCurrentGuess = "";
    private String userCurrentGuess = "";
    private boolean userWon;

    private boolean npcWon;

    // Check if rps round ends in tie
    private boolean roundTie;

    // fields for tracking user stats
    private int wins = 0;
    private int losses = 0;
    private int ties = 0;
    private int maxStreak = 0;
    private int currentStreak = 0;
    private int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        GAME_CHOICES.put(0, "ROCK");
        GAME_CHOICES.put(1, "PAPER");
        GAME_CHOICES.put(2, "SCISSORS");

        Intent intent = getIntent();
        userId = intent.getIntExtra(EXTRA_USER_ID, -1);

        Log.d(MainActivity.TAG, "Created gameplay activity for user "+userId);

        binding = ActivityGamePlayBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setupUserUI();
    }

    private void setupUserUI() {
        binding.rockPlayButton.setOnClickListener((v) -> {
            gamePlayButtonClickHandler(getString(R.string.rock_uppercase));
        });

        binding.paperPlayButton.setOnClickListener((v) -> {
            gamePlayButtonClickHandler(getString(R.string.paper_uppercase));
        });

        binding.scissorsPlayButton.setOnClickListener((v) -> {
            gamePlayButtonClickHandler(getString(R.string.scissors_uppercase));
        });

        binding.returnSelectableTextView.setOnClickListener((v) -> {
            // finish() returns user to the last view in the stack (home screen)
            finish();
        });
    }

    private void gamePlayButtonClickHandler(String choice) {
        setNpcChoice();
        setUserChoice(choice);
        int outcome = determineOutcome();
        updateGameplayUI();
        updateDatabaseStats(outcome);
    }

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

    private String outcomeToString(int outcome) {
        switch (outcome) {
            case UserStats.WIN: return "WIN";
            case UserStats.TIE: return "TIE";
            case UserStats.LOSE: return "LOSE";
            default: return "UNKNOWN";
        }
    }

    private void updateDatabaseStats(int outcome) {
        Log.d(MainActivity.TAG,"update db stats with outcome "+outcome);
        RockPaperScissorsRepository repository = RockPaperScissorsRepository.getRepository(getApplication());
        if (repository == null) {
            toastMaker("Failed to update the database due to db error.");
            return;
        }
        LiveData<UserStats> userStatsLiveData = repository.getUserStatsByUserId(userId);
        Observer<UserStats> userStatsObserver = new Observer<UserStats>() {
            @Override
            public void onChanged(UserStats userStats) {
                if (userStats == null) {
                    userStats = new UserStats(userId,0,0,0,0,0);
                }

                int userChoiceIndex = convertChoiceToIndex(userCurrentGuess);
                int npcChoiceIndex = convertChoiceToIndex(npcCurrentGuess);
                String resultString = outcomeToString(outcome);

                RpsRound round = new RpsRound(userId, userChoiceIndex, npcChoiceIndex, resultString);
                repository.insertRound(round);
                Log.d(MainActivity.TAG, "Inserted round: " + round);

                switch(outcome) {
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
                        Log.e(MainActivity.TAG, "Unhandled outcome option: " + outcome);
                }

                Log.d(MainActivity.TAG, "Updating user stats "+userStats);
                repository.insertOrUpdateUserStats(userStats);
                userStatsLiveData.removeObserver(this);
            }
        };
        userStatsLiveData.observeForever(userStatsObserver);

    }

    private void setUserChoice(String userChoice) {
        userCurrentGuess = userChoice;
    }

    // Function generates and sets an npcPlay to be set
    private void setNpcChoice() {
        int npc_guess = RpsRandomNumberGenerator.getRandomNumber();
        npcCurrentGuess = GAME_CHOICES.get(npc_guess);
    }

    // Helper function to determine of user won or lost (true, false)
    private int determineOutcome() {
        // determine all user winning cases, anything else means user lost
        if (userCurrentGuess.equals(GAME_CHOICES.get(0)) && npcCurrentGuess.equals(GAME_CHOICES.get(2))) {
            // user pick rock, npc pick scissorA
            roundTie = false; // redundant but for safety
            userWon = true;
            npcWon = false;
            return UserStats.WIN;
        }
        if (userCurrentGuess.equals(GAME_CHOICES.get(1)) && npcCurrentGuess.equals(GAME_CHOICES.get(0))) {
            // user pick paper, npc pick rock
            roundTie = false;
            userWon = true;
            npcWon = false;
            return UserStats.WIN;
        }
        if (userCurrentGuess.equals(GAME_CHOICES.get(2)) && npcCurrentGuess.equals(GAME_CHOICES.get(1))) {
            // user pick scissors, npc pick paper
            roundTie = false;
            userWon = true;
            npcWon = false;
            return UserStats.WIN;
        }
        // forgot to include a tie lol
        if (userCurrentGuess.equals(npcCurrentGuess)) {
            roundTie = true;
            userWon = false;
            npcWon = false;
            return UserStats.TIE;
        }
        roundTie = false;
        userWon = false;
        npcWon = true;
        return UserStats.LOSE;


    }

    private void updateGameplayUI() {
        binding.youChoseOutputTextView.setText(userCurrentGuess);
        binding.npcChoseOutputTextView.setText(npcCurrentGuess);
        if (userWon) {
            binding.resultOutputTextView.setText(R.string.you_win);
        }
        if (npcWon) {
            binding.resultOutputTextView.setText(R.string.you_lose);
        }
        if (roundTie) {
            binding.resultOutputTextView.setText(R.string.it_s_a_tie);
        }
    }

    // TODO: make into toastStats for debugging
    private void printStats() {
        System.out.printf(
                "Wins: %s Loses: %s Ties: %s Max Streak: %s Current Streak: %s",
                wins, losses, ties, maxStreak, currentStreak
        );
    }

    private void toastMaker(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    static Intent gamePlayActivityIntentFactory(Context context, int userId) {
        Intent intent = new Intent(context, GamePlayActivity.class);
        Log.d(MainActivity.TAG, "Creating gameplay intent for user "+userId);
        intent.putExtra(EXTRA_USER_ID, userId);
        return intent;
    }
}