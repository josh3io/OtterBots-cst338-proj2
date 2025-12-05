package edu.csumb.cst338.otterbots.rockpaperscissors;

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
            // TODO: Implement rock play logic

            // TODO: Implement npc play, updateNpcGuess, in each onclicklistner

            userCurrentGuess = GAME_CHOICES.get(0);
            setNpcChoice();
            setUserChoice(userCurrentGuess);

            //TODO: Implement result from rps round in UI, updateGameplayUI;
            // another idea is to instead handle UI updates in choice functions?

            // TODO: REMOVE THIS DEBUG TOAST
           toastMaker("NPC GUESS: " + npcCurrentGuess + " Player guess: " + userCurrentGuess);
           toastMaker("USER WON: " + userWon);

        });

        binding.paperPlayButton.setOnClickListener((v) -> {
            // TODO: Implement paper play logic
            userCurrentGuess = GAME_CHOICES.get(1);
            setNpcChoice();
            setUserChoice(userCurrentGuess);

            toastMaker("NPC GUESS: " + npcCurrentGuess + " Player guess: " + userCurrentGuess);
            toastMaker("USER WON: " + userWon);
        });

        binding.scissorsPlayButton.setOnClickListener((v) -> {
            // TODO: Implement scissors play logic
            userCurrentGuess = GAME_CHOICES.get(1);
            setNpcChoice();
            setUserChoice(userCurrentGuess);

            toastMaker("NPC GUESS: " + npcCurrentGuess + " Player guess: " + userCurrentGuess);
            toastMaker("USER WON: " + userWon);
        });

       binding.returnSelectableTextView.setOnClickListener((v) -> {
          // TODO: Implement return button function

           userCurrentGuess = GAME_CHOICES.get(2);
           setNpcChoice();
           setUserChoice(userCurrentGuess);

           toastMaker("NPC GUESS: " + npcCurrentGuess + " Player guess: " + userCurrentGuess);
           toastMaker("USER WON: " + userWon);
       });


    }
    private void setUserChoice(String userCurrentGuess) {
        userWon = determineWinner(userCurrentGuess, npcCurrentGuess);
    }

    // Function generates and sets an npcPlay to be set
     private void setNpcChoice() {
        int npc_guess = random.nextInt(GAME_CHOICES.size());
        npcCurrentGuess = GAME_CHOICES.get(npc_guess);
    }
    
   // TODO: Refractor to update total UI for finsihed round for both user and Npc
    // TODO: Handle binding logic to update NPC guess text after user sets their play
        // Helper function to set text Npc Guess
    private void updateNpcPlay(ActivityGamePlayBinding binding) {

    }
    // Helper function to determine of user won or lost (true, false)
    private boolean determineWinner(String userGuess, String npcGuess) {
        // determine all winning cases, anything else means user lost
        if (userGuess.equals(GAME_CHOICES.get(0)) && npcGuess.equals(GAME_CHOICES.get(2))){
           // user pick rock, npc pick scissor
            return true;
        }
        if (userGuess.equals(GAME_CHOICES.get(1)) && npcGuess.equals(GAME_CHOICES.get(0))){
            // user pick paper, npc pick rock
            return true;
        }
        if (userGuess.equals(GAME_CHOICES.get(2)) && npcGuess.equals(GAME_CHOICES.get(1))){
            // user pick scissors, npc pick paper
            return true;
        }
        return false;
    }


     private void toastMaker(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}