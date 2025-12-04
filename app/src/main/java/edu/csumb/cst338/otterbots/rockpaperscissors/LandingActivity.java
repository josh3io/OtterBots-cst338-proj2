package edu.csumb.cst338.otterbots.rockpaperscissors;

import static edu.csumb.cst338.otterbots.rockpaperscissors.LoginActivity.EXTRA_IS_ADMIN;
import static edu.csumb.cst338.otterbots.rockpaperscissors.LoginActivity.EXTRA_USERNAME;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import edu.csumb.cst338.otterbots.rockpaperscissors.databinding.ActivityLandingAdminBinding;
import edu.csumb.cst338.otterbots.rockpaperscissors.databinding.ActivityLandingUserBinding;

public class LandingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        // Get data from LoginActivity
        Intent intent = getIntent();
        String userName = intent.getStringExtra(EXTRA_USERNAME);
        boolean isAdmin = intent.getBooleanExtra(EXTRA_IS_ADMIN, false);

        if (userName == null || userName.trim().isEmpty()) {
            userName = "Player"; // So UI doesn't look broken
        }

        if (isAdmin) {
            // Inflate Admin layout and set it as the content view
            ActivityLandingAdminBinding binding = ActivityLandingAdminBinding.inflate(getLayoutInflater());
            setContentView(binding.getRoot());
            setupAdminUI(binding, userName);
        } else {
            // Inflate User layout and set it as the content view
            ActivityLandingUserBinding binding = ActivityLandingUserBinding.inflate(getLayoutInflater());
            setContentView(binding.getRoot());
            setupUserUI(binding, userName);
        }
    }

    private void setupUserUI(ActivityLandingUserBinding binding, String userName) {
        // Welcome Text
        binding.titleLandingTextView.setText("Welcome\n" + userName);

        //TODO: Pull real stats from DB instead of just labels.

        // Start New Game
        binding.startNewGameButton.setOnClickListener(v -> {
            // TODO: Replace toast with navigation to Game Activity.
            toastMaker("Start New Game (user)");
        });

        binding.viewLeaderboardTextView.setOnClickListener(v -> {
            // TODO: Replace toast with navigation to Leaderboard Activity.
            toastMaker("View Leaderboard (user)");
        });

        // Logout
        binding.logoutTextView.setOnClickListener(v -> logout());
    }

    private void setupAdminUI(ActivityLandingAdminBinding binding, String userName) {
        // Welcome text: Show Admin + userName
        binding.titleLandingTextView.setText("Welcome\nOtterBot " + userName);

        // TODO: Pull real stats from DB instead of just labels.

        // Start New Game
        binding.startNewGameButton.setOnClickListener(v -> {
            // TODO: Replace toast with navigation to Game Activity.
            toastMaker("Start New Game (admin)");
        });

        binding.viewLeaderboardTextView.setOnClickListener(v -> {
            // TODO: Replace toast with navigation to Leaderboard Activity.
            toastMaker("View Leaderboard (admin)");
        });

        // Add User (admin-only feature)
        binding.addUserTextView.setOnClickListener(v -> {
            // TODO: Replace toast with navigation to addUser Activity.
            toastMaker("Add User (admin)");
        });

        // Logout
        binding.logoutTextView.setOnClickListener(v -> logout());
    }

    private void logout() {
        Intent logoutIntent = new Intent(this, LoginActivity.class);

        // Clear back stack so Back doesn't return to Landing
        logoutIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(logoutIntent);
        finish();
    }

    private void toastMaker(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}