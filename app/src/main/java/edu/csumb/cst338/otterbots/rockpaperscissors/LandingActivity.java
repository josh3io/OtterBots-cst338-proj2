package edu.csumb.cst338.otterbots.rockpaperscissors;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import edu.csumb.cst338.otterbots.rockpaperscissors.api.RpsRandomNumberGenerator;
import edu.csumb.cst338.otterbots.rockpaperscissors.database.entities.RockPaperScissorsRepository;
import edu.csumb.cst338.otterbots.rockpaperscissors.database.entities.UserStats;
import edu.csumb.cst338.otterbots.rockpaperscissors.databinding.ActivityLandingAdminBinding;
import edu.csumb.cst338.otterbots.rockpaperscissors.databinding.ActivityLandingUserBinding;

/**
 * LandingActivity
 * <p>
 * Central landing screen shown after a successful login. It:
 * <ul>
 *   <li>Chooses between the normal user layout and admin layout.</li>
 *   <li>Displays basic stats for the logged-in user.</li>
 *   <li>Provides navigation to gameplay, leaderboard, account changes, and admin tools.</li>
 * </ul>
 *
 * <p>Author: Anthony Martinez</p>
 */
public class LandingActivity extends AppCompatActivity {

  /** Intent key for the username of the logged-in user. */
  public static final String EXTRA_USERNAME = "username";

  /** Intent key for the userId of the logged-in user. */
  public static final String EXTRA_USER_ID = "userId";

  /** Intent key indicating whether the logged-in user is an admin. */
  public static final String EXTRA_IS_ADMIN = "isAdmin";

  /** Shared log tag for landing-related events. */
    public static final String TAG = "Otterbots.RPS";

  /** Currently logged-in user's ID. */
  private int userId;

  /** Flag indicating whether the current user is an admin. */
  private boolean isAdmin;

  /** Repository providing access to database operations. */
  private RockPaperScissorsRepository repository;

  /** View binding for the normal user layout. */
  private ActivityLandingUserBinding userBinding;

  /** View binding for the admin layout. */
  private ActivityLandingAdminBinding adminBinding;

  /**
   * Factory method to create an {@link Intent} for launching {@link LandingActivity}.
   *
   * @param context the context used to create the intent
   * @param username the logged-in user's username
   * @param userId the logged-in user's unique ID
   * @param isAdmin whether the user has admin privileges
   * @return an Intent configured to start LandingActivity
   */
  public static Intent createIntent(Context context, String username, int userId, boolean isAdmin) {
    Intent intent = new Intent(context, LandingActivity.class);
    intent.putExtra(EXTRA_USERNAME, username);
    intent.putExtra(EXTRA_USER_ID, userId);
    intent.putExtra(EXTRA_IS_ADMIN, isAdmin);
    return intent;
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {

    super.onCreate(savedInstanceState);

    // Initialize repository for database operations.
    repository = RockPaperScissorsRepository.getRepository(getApplication());

    // Get data from LoginActivity via Intent extras.
    Intent intent = getIntent();
    String userName = intent.getStringExtra(EXTRA_USERNAME);
    userId = intent.getIntExtra(EXTRA_USER_ID, -1);
    isAdmin = intent.getBooleanExtra(EXTRA_IS_ADMIN, false);

    Log.d(TAG, "Landing with userId " + userId);

    // Fallback name so the UI never looks broken or empty.
    if (userName == null || userName.trim().isEmpty()) {
      userName = getString(R.string.default_player_name); // So UI doesn't look broken
    }

    // Inflate the correct layout based on admin status.
    if (isAdmin) {
      adminBinding = ActivityLandingAdminBinding.inflate(getLayoutInflater());
      setContentView(adminBinding.getRoot());
      setupAdminUI(adminBinding, userName);
    } else {
      userBinding = ActivityLandingUserBinding.inflate(getLayoutInflater());
      setContentView(userBinding.getRoot());
      setupUserUI(userBinding, userName);
    }

    // Observe the latest version of this user so name changes are reflected on the screen.
    if (userId != -1) {
      repository.getUserById(userId).observe(this, user -> {
        if (user == null) {
          return;
        }

        String latestName = user.getUsername();
        if (latestName == null || latestName.trim().isEmpty()) {
          latestName = getString(R.string.default_player_name);
        }

        if (isAdmin && adminBinding != null) {
          adminBinding.titleLandingTextView.setText(getString(R.string.welcome_admin, latestName));
        } else if (!isAdmin && userBinding != null) {
          userBinding.titleLandingTextView.setText(getString(R.string.welcome_user, latestName));
        }
      });
    }

    // Initialize the random number generator for gameplay
    RpsRandomNumberGenerator.initializeGenerator();
    /* Example usage:
     *  int number = RpsRandomNumberGenerator.getRandomNumber();
     */
  }

  /**
   * Shared helper that loads {@link UserStats} for {@link #userId} and binds values
   * into the provided {@link TextView}s for display.
   *
   * <p>If no stats exist yet for the user, a default stats row is created.</p>
   *
   * @param winsView TextView to display total wins
   * @param lossesView TextView to display total losses
   * @param tiesView TextView to display total ties
   * @param maxStreakView TextView to display the maximum win streak
   * @param currentStreakView TextView to display the current streak
   * @param gamesPlayedView TextView to display total games played
   */
  private void loadAndBindUserStats(TextView winsView,
      TextView lossesView,
      TextView tiesView,
      TextView maxStreakView,
      TextView currentStreakView,
      TextView gamesPlayedView) {
    repository.getUserStatsByUserId(userId).observe(this, stats -> {
      if (stats == null) {
        // No stats yet for this user -> create default row and let future observations update UI.
        UserStats newStats = new UserStats(
            userId,
            0,
            0,
            0,
            0,
            0
        );
        repository.insertOrUpdateUserStats(newStats);
        return;
      }

      // Total games played
      int gamesPlayed = stats.getWins() + stats.getLosses() + stats.getTies();

      // Update labels with the actual numbers
      winsView.setText(getString(R.string.wins) + " " + stats.getWins());
      lossesView.setText(getString(R.string.losses) + " " + stats.getLosses());
      tiesView.setText(getString(R.string.ties) + " " + stats.getTies());
      maxStreakView.setText(getString(R.string.max_streak) + " " + stats.getMaxStreak());
      currentStreakView.setText(
          getString(R.string.current_streak) + " " + stats.getCurrentStreak());
      gamesPlayedView.setText(getString(R.string.games_played) + " " + gamesPlayed);
    });
  }

  /**
   * Configures the UI behavior for a non-admin user landing screen:
   * <ul>
   *   <li>Displays a personalized welcome message.</li>
   *   <li>Loads and displays user statistics.</li>
   *   <li>Provides navigation to gameplay, leaderboard, and user details update.</li>
   *   <li>Provides a logout action.</li>
   * </ul>
   *
   * @param binding view binding for the user layout
   * @param userName name of the logged-in user to display in the welcome text
   */
  private void setupUserUI(ActivityLandingUserBinding binding, String userName) {
    // Welcome Text
    binding.titleLandingTextView.setText(getString(R.string.welcome_user, userName));

    // Load stats into user layout labels
    loadAndBindUserStats(
        binding.winsLabelTextView,
        binding.lossesLabelTextView,
        binding.tiesLabelTextView,
        binding.maxStreakLabelTextView,
        binding.currentStreakLabelTextView,
        binding.gamesPlayedTextView
    );

    // Start New Game
    binding.startNewGameButton.setOnClickListener(v -> {
      Intent intent = GamePlayActivity.gamePlayActivityIntentFactory(this, userId);
      startActivity(intent);
    });

    // View leaderboard
    binding.viewLeaderboardTextView.setOnClickListener(v -> {
      Intent intent = LeaderboardActivity.leaderboardActivityIntentFactory(
          binding.getRoot().getContext(),
          userName,
          userId,
          false
      );
      startActivity(intent);
    });

    // Navigate to change user details
    binding.changeUserDetailsTextView.setOnClickListener(v -> {
      Intent intent = UpdateUserDetails.createIntent(this, userId);
      startActivity(intent);
    });
    // Logout
    binding.logoutTextView.setOnClickListener(v -> logout());
  }

  /**
   * Configures the UI behavior for an admin user landing screen:
   * <ul>
   *   <li>Displays an admin-specific welcome message.</li>
   *   <li>Loads and displays user statistics.</li>
   *   <li>Provides navigation to gameplay and leaderboard (with admin flag).</li>
   *   <li>Provides admin-only actions for deleting and creating users.</li>
   *   <li>Provides a logout action.</li>
   * </ul>
   *
   * @param binding view binding for the admin layout
   * @param userName name of the logged-in admin for the welcome message
   */
  private void setupAdminUI(ActivityLandingAdminBinding binding, String userName) {
    // Welcome text: Show Admin + userName
    binding.titleLandingTextView.setText(getString(R.string.welcome_admin, userName));

    // Load stats into admin layout labels
    loadAndBindUserStats(
        binding.winsLabelTextView,
        binding.lossesLabelTextView,
        binding.tiesLabelTextView,
        binding.maxStreakLabelTextView,
        binding.currentStreakLabelTextView,
        binding.gamesPlayedTextView
    );

    // Start New Game
    binding.startNewGameButton.setOnClickListener(v -> {
      Intent intent = GamePlayActivity.gamePlayActivityIntentFactory(this, userId);
      startActivity(intent);
    });

    // View leaderboard (with admin flag set to true)
    binding.viewLeaderboardTextView.setOnClickListener(v -> {
      Intent intent = LeaderboardActivity.leaderboardActivityIntentFactory(
          binding.getRoot().getContext(),
          userName,
          userId,
          true
      );
      startActivity(intent);
    });

    // Delete Users (admin-only feature)
    binding.deleteUserTextView.setOnClickListener(v -> {
      Intent intent = AdminDeleteUserActivity.createIntent(this);
      startActivity(intent);

      toastMaker(getString(R.string.delete_user_admin));
    });

    // Add User (admin-only feature)
    binding.addUserTextView.setOnClickListener(v -> {
      Intent intent = AdminNewUserActivity.createIntent(this);
      startActivity(intent);

      toastMaker(getString(R.string.toast_add_user_admin));
    });

    // Navigate to change user details
    binding.changeUserDetailsTextView.setOnClickListener(v -> {
      Intent intent = UpdateUserDetails.createIntent(this, userId);
      startActivity(intent);
    });

    // Logout
    binding.logoutTextView.setOnClickListener(v -> logout());
  }

  /**
   * Logs the user out by launching {@link LoginActivity} and finishing this activity,
   * clearing it from the back stack.
   */
  private void logout() {
    Intent logoutIntent = LoginActivity.createLogoutIntent(this);
    startActivity(logoutIntent);
    finish();
  }

  /**
   * Convenience helper for displaying a short {@link Toast} message.
   *
   * @param message text to display in the toast
   */
  private void toastMaker(String message) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
  }
}