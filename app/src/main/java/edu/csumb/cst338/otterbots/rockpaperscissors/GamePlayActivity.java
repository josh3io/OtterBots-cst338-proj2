package edu.csumb.cst338.otterbots.rockpaperscissors;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import edu.csumb.cst338.otterbots.rockpaperscissors.databinding.ActivityGamePlayBinding;

public class GamePlayActivity extends AppCompatActivity {

    Random random = new Random();
    private HashMap<Integer, String> GAME_CHOICES = new HashMap<>();
    private String npcCurrentGuess = "";
    private String userCurrentGuess = "";

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
            // TODO: REMOVE THIS DEBUG TOAST

            // TODO: Implement npc play, updateNpcGuess, in each onclicklistner
            npcPlay();
           toastMaker("NPC GUESS: " + npcCurrentGuess + " Player guess: Rock");

        });

        binding.paperPlayButton.setOnClickListener((v) -> {
            // TODO: Implement paper play logic
            toastMaker("Player selected paper");
        });

        binding.scissorsPlayButton.setOnClickListener((v) -> {
            // TODO: Implement scissors play logic
            toastMaker("Player selected scissors");
        });

       binding.returnSelectableTextView.setOnClickListener((v) -> {
          // TODO: Implement return button function
           toastMaker("Player selected return");
       });


    }

    // Function generates and sets an npcPlay to be set
     private void npcPlay() {
        int npc_guess = random.nextInt(GAME_CHOICES.size());
        npcCurrentGuess = GAME_CHOICES.get(npc_guess);
    }
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