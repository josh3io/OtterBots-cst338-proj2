package edu.csumb.cst338.otterbots.rockpaperscissors;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.HashMap;

import edu.csumb.cst338.otterbots.rockpaperscissors.api.RpsRandomNumberGenerator;
import edu.csumb.cst338.otterbots.rockpaperscissors.databinding.ActivityGamePlayBinding;

public class GamePlayActivity extends AppCompatActivity {

    private HashMap<Integer, String> GAME_CHOICES = new HashMap<>();
    private String npcCurrentGuess = "";
    private String userCurrentGuess = "";
    private boolean userWon;

    private boolean npcWon;

    // fields for database update userStats
    // Check if rps round ends in tie
    private boolean roundTie;

    // fields for tracking user stats
    private int wins = 0;
    private int losses = 0;
    private int ties = 0;
    private int maxStreak = 0;
    private int currentStreak = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        GAME_CHOICES.put(0, "ROCK");
        GAME_CHOICES.put(1, "PAPER");
        GAME_CHOICES.put(2, "SCISSORS");



       ActivityGamePlayBinding binding = ActivityGamePlayBinding.inflate(getLayoutInflater());
       setContentView(binding.getRoot());
       setupUserUI(binding);
    }

    private void setupUserUI(ActivityGamePlayBinding binding) {
        binding.rockPlayButton.setOnClickListener((v) -> {

            setNpcChoice();
            setUserChoice("ROCK");
            determineWinner();
            updateGameplayUI(binding);
        });

        binding.paperPlayButton.setOnClickListener((v) -> {
            setNpcChoice();
            setUserChoice("PAPER");
            determineWinner();
            updateGameplayUI(binding);

        });

        binding.scissorsPlayButton.setOnClickListener((v) -> {

            setNpcChoice();
            setUserChoice("SCISSORS");
            determineWinner();
            updateGameplayUI(binding);
        });

       binding.returnSelectableTextView.setOnClickListener((v) -> {
           // finish() returns user to the last view in the stack (home screen)
           finish();
       });
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
    private void determineWinner() {
        // determine all user winning cases, anything else means user lost
        if (userCurrentGuess.equals(GAME_CHOICES.get(0)) && npcCurrentGuess.equals(GAME_CHOICES.get(2))){
           // user pick rock, npc pick scissorA
            roundTie = false; // redundant but for safety
            userWon = true;
            wins = wins + 1;
            npcWon = false;
            updateStreak();
            printStats();
            return;
        }
        if (userCurrentGuess.equals(GAME_CHOICES.get(1)) && npcCurrentGuess.equals(GAME_CHOICES.get(0))){
            // user pick paper, npc pick rock
            roundTie = false;
            userWon = true;
            wins = wins + 1;
            npcWon = false;
            updateStreak();
            printStats();
            return;
        }
        if (userCurrentGuess.equals(GAME_CHOICES.get(2)) && npcCurrentGuess.equals(GAME_CHOICES.get(1))){
            // user pick scissors, npc pick paper
            roundTie = false;
            userWon = true;
            wins = wins + 1;
            npcWon = false;
            updateStreak();
            printStats();
            return;
        }
        // forgot to include a tie lol
        if (userCurrentGuess.equals(npcCurrentGuess)) {
            roundTie = true;
            userWon = false;
            npcWon = false;
            ties = ties + 1;
            updateStreak();
            printStats();
            return;
        }
        roundTie = false;
        userWon = false;
        npcWon = true;
        losses = losses + 1;
        currentStreak = 0;
        printStats();
    }

    // helper function to update streaks
    private void updateStreak() {
        if (wins > currentStreak) {
            currentStreak = wins;
            if (currentStreak > maxStreak) {
                maxStreak = currentStreak;
            }
        }
    }

    private void updateGameplayUI(ActivityGamePlayBinding binding){
        binding.youChoseOutputTextView.setText(userCurrentGuess);
        binding.npcChoseOutputTextView.setText(npcCurrentGuess);
        if (userWon){
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
    private void printStats(){
        System.out.printf("Wins: %s Loses: %s Ties: %s Max Streak: %s Current Streak: %s",
                wins, losses, ties, maxStreak, currentStreak);
    }
     private void toastMaker(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}