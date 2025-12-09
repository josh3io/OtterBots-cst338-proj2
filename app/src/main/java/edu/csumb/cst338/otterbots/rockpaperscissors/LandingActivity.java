package edu.csumb.cst338.otterbots.rockpaperscissors;

import static edu.csumb.cst338.otterbots.rockpaperscissors.LoginActivity.EXTRA_IS_ADMIN;
import static edu.csumb.cst338.otterbots.rockpaperscissors.LoginActivity.EXTRA_USERNAME;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import edu.csumb.cst338.otterbots.rockpaperscissors.databinding.ActivityLandingAdminBinding;
import edu.csumb.cst338.otterbots.rockpaperscissors.databinding.ActivityLandingUserBinding;

public class LandingActivity extends AppCompatActivity {

    public static Intent createIntent(Context context, String userName, boolean isAdmin) {
        Intent intent = new Intent(context, LandingActivity.class);
        intent.putExtra(EXTRA_USERNAME, userName);
        intent.putExtra(EXTRA_IS_ADMIN, isAdmin);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        // Get data from LoginActivity
        Intent intent = getIntent();
        String userName = intent.getStringExtra(EXTRA_USERNAME);
        boolean isAdmin = intent.getBooleanExtra(EXTRA_IS_ADMIN, false);

        if (userName == null || userName.trim().isEmpty()) {
            userName = getString(R.string.default_player_name); // So UI doesn't look broken
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
        binding.titleLandingTextView.setText(getString(R.string.welcome_user, userName));

        //TODO: Pull real stats from DB instead of just labels.

        // Start New Game
        binding.startNewGameButton.setOnClickListener(v -> {
            // TODO: Replace toast with navigation to Game Activity.
            toastMaker(getString(R.string.toast_start_new_game_user));
        });

        binding.viewLeaderboardTextView.setOnClickListener(v -> {
            Intent intent = LeaderboardActivity.leaderboardActivityIntentFactory(binding.getRoot().getContext(), userName, false);
            startActivity(intent);
        });

        // Logout
        binding.logoutTextView.setOnClickListener(v -> logout());
    }

    private void setupAdminUI(ActivityLandingAdminBinding binding, String userName) {
        // Welcome text: Show Admin + userName
        binding.titleLandingTextView.setText(getString(R.string.welcome_admin, userName));

        // TODO: Pull real stats from DB instead of just labels.

        // Start New Game
        binding.startNewGameButton.setOnClickListener(v -> {
            // TODO: Replace toast with navigation to Game Activity.
            toastMaker(getString(R.string.toast_start_new_game_admin));
        });

        binding.viewLeaderboardTextView.setOnClickListener(v -> {
            Intent intent = LeaderboardActivity.leaderboardActivityIntentFactory(binding.getRoot().getContext(), userName, true);
            startActivity(intent);
        });

        // Add User (admin-only feature)
        binding.addUserTextView.setOnClickListener(v -> {
            // TODO: Replace toast with navigation to ADMIN addUser Activity.
            toastMaker(getString(R.string.toast_add_user_admin));
        });

        // Logout
        binding.logoutTextView.setOnClickListener(v -> logout());
    }

    private void logout() {
        Intent logoutIntent = LoginActivity.createLogoutIntent(this);
        startActivity(logoutIntent);
        finish();
    }

    private void toastMaker(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}