package edu.csumb.cst338.otterbots.rockpaperscissors;

import android.net.ipsec.ike.TunnelModeChildSessionParams;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.HashMap;
import java.util.Random;

import edu.csumb.cst338.otterbots.rockpaperscissors.databinding.ActivityGamePlayBinding;

public class GamePlayActivity extends AppCompatActivity {

    Random random = new Random();
    private HashMap<Integer, String> GAME_CHOICES = new HashMap<>();
    private String npcCurrentGuess = "";
    private String userCurrentGuess = "";
    private boolean userWon;

    private boolean npcWon;

    // Check if rps round ends in tie
    private boolean roundTie;

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

    // TODO: REMOVE THIS DEBUG TOAST
    private void setupUserUI(ActivityGamePlayBinding binding) {
        binding.rockPlayButton.setOnClickListener((v) -> {
            // TODO: Implement rock play logic

            // TODO: Implement npc play, updateNpcGuess, in each onclicklistner

            setNpcChoice();
            setUserChoice("ROCK");
            determineWinner();

            //TODO: Implement result from rps round in UI, updateGameplayUI;
            // another idea is to instead handle UI updates in choice functions?

           toastMaker("NPC GUESS: " + npcCurrentGuess + " Player guess: " + userCurrentGuess);
           toastMaker("USER WON: " + userWon);

        });

        binding.paperPlayButton.setOnClickListener((v) -> {
            setNpcChoice();
            setUserChoice("PAPER");
            determineWinner();

            //TODO: add updateGameplayUI();

            toastMaker("NPC GUESS: " + npcCurrentGuess + " Player guess: " + userCurrentGuess);
            toastMaker("USER WON: " + userWon);
        });

        binding.scissorsPlayButton.setOnClickListener((v) -> {
            // TODO: Implement scissors play logic
            setNpcChoice();
            setUserChoice("SCISSORS");
            determineWinner();

            toastMaker("NPC GUESS: " + npcCurrentGuess + " Player guess: " + userCurrentGuess);
            toastMaker("USER WON: " + userWon);
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
        int npc_guess = random.nextInt(GAME_CHOICES.size());
        npcCurrentGuess = GAME_CHOICES.get(npc_guess);
    }
    
   // TODO: Refractor to update total UI for finsihed round for both user and Npc
    // TODO: Handle binding logic to update NPC guess text after user sets their play

    // Helper function to determine of user won or lost (true, false)
    private void determineWinner() {
        // determine all user winning cases, anything else means user lost
        if (userCurrentGuess.equals(GAME_CHOICES.get(0)) && npcCurrentGuess.equals(GAME_CHOICES.get(2))){
           // user pick rock, npc pick scissorA
            roundTie = false; // redundant but for safety
            userWon = true;
            npcWon = false;
            return;
        }
        if (userCurrentGuess.equals(GAME_CHOICES.get(1)) && npcCurrentGuess.equals(GAME_CHOICES.get(0))){
            // user pick paper, npc pick rock
            roundTie = false;
            userWon = true;
            npcWon = false;
            return;
        }
        if (userCurrentGuess.equals(GAME_CHOICES.get(2)) && npcCurrentGuess.equals(GAME_CHOICES.get(1))){
            // user pick scissors, npc pick paper
            roundTie = false;
            userWon = true;
            npcWon = false;
            return;
        }
        // forgot to include a tie lol
        if (userCurrentGuess.equals(npcCurrentGuess)) {
            roundTie = true;
            userWon = false;
            npcWon = false;
            toastMaker("Looks like its a tie");
            return;
        }
        roundTie = false;
        userWon = false;
        npcWon = true;

    }


     private void toastMaker(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}