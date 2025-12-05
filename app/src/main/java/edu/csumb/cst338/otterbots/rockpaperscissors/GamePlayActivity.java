package edu.csumb.cst338.otterbots.rockpaperscissors;

import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import edu.csumb.cst338.otterbots.rockpaperscissors.databinding.ActivityGamePlayBinding;

public class GamePlayActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

       ActivityGamePlayBinding binding = ActivityGamePlayBinding.inflate(getLayoutInflater());
       setContentView(binding.getRoot());
       setupUserUI(binding);
    }

    public void setupUserUI(ActivityGamePlayBinding binding) {
        binding.rockPlayButton.setOnClickListener((v) -> {
            // TODO: Implement rock play logic
           toastMaker("Player selected rock");
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

    public void toastMaker(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}