package edu.csumb.cst338.otterbots.rockpaperscissors;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.content.Intent;

import edu.csumb.cst338.otterbots.rockpaperscissors.database.entities.RockPaperScissorsRepository;
import edu.csumb.cst338.otterbots.rockpaperscissors.databinding.ActivityResetStatsBinding;

public class ResetStats extends AppCompatActivity {

    private RockPaperScissorsRepository repository;
    private int userId;
    private ActivityResetStatsBinding binding;
    public static final String EXTRA_USER_ID = "extra_user_id";
    public static final String EXTRA_USERNAME = "extra_username";

    public static Intent createIntent(Context context, int userId, String username) {
        Intent intent = new Intent(context, ResetStats.class);
        intent.putExtra(EXTRA_USER_ID, userId);
        intent.putExtra(EXTRA_USERNAME, username);
        return intent;
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityResetStatsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        repository = RockPaperScissorsRepository.getRepository(getApplication());
        userId = getIntent().getIntExtra(EXTRA_USER_ID, -1);

        String username = getIntent().getStringExtra(EXTRA_USERNAME);

        if (username == null || username.trim().isEmpty()) {
            username = getString(R.string.default_player_name);
        }

        binding.Title.setText(username + " " + getString(R.string.stats_title));

        if (userId != -1) {
            repository.getUserById(userId).observe(this, user -> {
                if (user == null) {
                    return;
                }

                String latestName = user.getUsername();
                if (latestName == null || latestName.trim().isEmpty()) {
                    latestName = getString(R.string.default_player_name);
                }

                binding.Title.setText(latestName + " " + getString(R.string.stats_title));
            });
        }

        repository.getUserStatsByUserId(userId).observe(this, stats -> {
            if (stats == null) return;

            int gamesPlayed = stats.getWins() + stats.getLosses() + stats.getTies();

            binding.WinsLabel.setText(getString(R.string.wins) + " " + stats.getWins());
            binding.LossesLabel.setText(getString(R.string.losses) + " " + stats.getLosses());
            binding.TiesLabel.setText(getString(R.string.ties) + " " + stats.getTies());
            binding.CurrentStreakLabel.setText(getString(R.string.current_streak) + " " + stats.getCurrentStreak());
            binding.MaxStreakLabel.setText(getString(R.string.max_streak) + " " + stats.getMaxStreak());
            binding.GamesPlayedLabel.setText(getString(R.string.games_played) + " " + gamesPlayed);
        });

        binding.ConfirmResetButton.setOnClickListener(v -> {
            if (userId == -1) return;
            repository.resetStatsForUser(userId);
        });

        binding.ReturnButton.setOnClickListener(v -> finish());
    }
}