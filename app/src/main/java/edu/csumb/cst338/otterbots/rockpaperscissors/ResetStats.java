package edu.csumb.cst338.otterbots.rockpaperscissors;
/**
 * Description: Activity for resetting user statistics in the Rock-Paper-Scissors game.
 * Author: Murtaza Badri
 * Since: 2025.12.13
 */

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import edu.csumb.cst338.otterbots.rockpaperscissors.database.entities.RockPaperScissorsRepository;
import edu.csumb.cst338.otterbots.rockpaperscissors.databinding.ActivityResetStatsBinding;

public class ResetStats extends AppCompatActivity {

  // Intent extras for passing user information
  public static final String EXTRA_USER_ID = "extra_user_id";
  public static final String EXTRA_USERNAME = "extra_username";
  private RockPaperScissorsRepository repository;
  private int userId;
  private ActivityResetStatsBinding binding;

// Intent factory for launching ResetStats with userId and username
  public static Intent createIntent(Context context, int userId, String username) {
    Intent intent = new Intent(context, ResetStats.class);
    intent.putExtra(EXTRA_USER_ID, userId);
    intent.putExtra(EXTRA_USERNAME, username);
    return intent;
  }

  // onCreate method to set up the activity
  @SuppressLint("SetTextI18n")
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    // Set up ViewBinding
    binding = ActivityResetStatsBinding.inflate(getLayoutInflater());
    setContentView(binding.getRoot());

    // Initialize repository and retrieve userId and username
    repository = RockPaperScissorsRepository.getRepository(getApplication());
    userId = getIntent().getIntExtra(EXTRA_USER_ID, -1);

    String username = getIntent().getStringExtra(EXTRA_USERNAME);


    // Set the title with the username
    if (username == null || username.trim().isEmpty()) {
      username = getString(R.string.default_player_name);
    }

    binding.Title.setText(username + " " + getString(R.string.stats_title));

    // Observe user data to update title if username changes
    if (userId != -1) {
      repository.getUserById(userId).observe(this, user -> {
        if (user == null) {
          return;
        }
        // Update title with latest username
        String latestName = user.getUsername();
        if (latestName == null || latestName.trim().isEmpty()) {
          latestName = getString(R.string.default_player_name);
        }

        binding.Title.setText(latestName + " " + getString(R.string.stats_title));
      });
    }
    // Observe user stats to display current statistics
    repository.getUserStatsByUserId(userId).observe(this, stats -> {
      if (stats == null) {
        return;
      }
      // Calculate total games played
      int gamesPlayed = stats.getWins() + stats.getLosses() + stats.getTies();
      // Update UI labels with statistics
      binding.WinsLabel.setText(getString(R.string.wins) + " " + stats.getWins());
      binding.LossesLabel.setText(getString(R.string.losses) + " " + stats.getLosses());
      binding.TiesLabel.setText(getString(R.string.ties) + " " + stats.getTies());
      binding.CurrentStreakLabel.setText(
          getString(R.string.current_streak) + " " + stats.getCurrentStreak());
      binding.MaxStreakLabel.setText(getString(R.string.max_streak) + " " + stats.getMaxStreak());
      binding.GamesPlayedLabel.setText(getString(R.string.games_played) + " " + gamesPlayed);
    });
    // Set up button click listeners
    binding.ConfirmResetButton.setOnClickListener(v -> {
      if (userId == -1) {
        return;
      }
      repository.resetStatsForUser(userId);
    });
    // Return to previous screen
    binding.ReturnButton.setOnClickListener(v -> finish());
  }
}