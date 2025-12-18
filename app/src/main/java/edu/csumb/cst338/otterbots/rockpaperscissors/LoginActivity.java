package edu.csumb.cst338.otterbots.rockpaperscissors;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import edu.csumb.cst338.otterbots.rockpaperscissors.database.entities.RockPaperScissorsRepository;
import edu.csumb.cst338.otterbots.rockpaperscissors.databinding.ActivityLoginBinding;

/**
 * Login screen for the RockPaperScissors app.
 *
 * <p>Responsibilities:
 * <ul>
 *   <li>Collects username and password from the user.</li>
 *   <li>Validates that both fields are non-empty.</li>
 *   <li>Looks up the user in the repository and verifies the password.</li>
 *   <li>Persists the last successfully authenticated username.</li>
 *   <li>Navigates to {@link LandingActivity} on successful login.</li>
 * </ul>
 *
 * <p>Author: Anthony Martinez
 */
public class LoginActivity extends AppCompatActivity {

  /** SharedPreferences file name for simple login-related state. */
  public static final String PREFS_NAME = "otterbots_prefs";

  /** Key for storing the last successfully authenticated username. */
  public static final String KEY_LAST_USERNAME = "last_username";
  private ActivityLoginBinding binding;
  private RockPaperScissorsRepository repository;

  /**
   * Creates an {@link Intent} used when logging out back to the login screen.
   *
   * <p>The flags clear the current task so that the back stack does not return to authenticated
   * screens after logout.
   *
   * @param context calling context
   * @return configured {@code Intent} targeting {@link LoginActivity}
   */
  public static Intent createLogoutIntent(Context context) {
    Intent logoutIntent = new Intent(context, LoginActivity.class);
    logoutIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
    return logoutIntent;
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    // View binding for layout access.
    binding = ActivityLoginBinding.inflate(getLayoutInflater());
    setContentView(binding.getRoot());

    // Prefill username with the last successfully authenticated user, if any.
    SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
    String saved = sharedPreferences.getString(KEY_LAST_USERNAME, "");
    if (!saved.isEmpty()) {
      binding.userNameLoginEditText.setText(saved);
    }

    repository = RockPaperScissorsRepository.getRepository(getApplication());

    // Attempt login when the login button is pressed.
    binding.loginButton.setOnClickListener(v -> verifyUser());

    // Navigate to the registration screen.
    binding.registerTextView.setOnClickListener(
        v -> startActivity(RegisterActivity.createRegisterIntent(v.getContext())));
  }

  /**
   * Validates login input and attempts to authenticate the user.
   *
   * <p>Steps:
   * <ol>
   *   <li>Ensure username and password are not blank.</li>
   *   <li>Look up the user by username.</li>
   *   <li>If found, verify the password matches.</li>
   *   <li>Persist the username and navigate to {@link LandingActivity} on success.</li>
   * </ol>
   */
  private void verifyUser() {
    String username = binding.userNameLoginEditText.getText().toString().trim();
    if (username.isEmpty()) {
      toastMaker(getString(R.string.error_username_blank));
      return;
    }

    String userPassword = binding.passwordLoginEditText.getText().toString().trim();
    if (userPassword.isEmpty()) {
      toastMaker(getString(R.string.error_password_blank));
      return;
    }

    // Look up user by username, then validate the password in code.
    repository.getUserByUsername(username).observe(this, user -> {
      if (user == null) {
        // No user with that username
        toastMaker(getString(R.string.invalid_username));
        binding.userNameLoginEditText.setSelection(0);
        return;
      }

      // Save only usernames that have been validated against the DB.
      SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
      prefs.edit().putString(KEY_LAST_USERNAME, username).apply();

      if (!userPassword.equals(user.getPassword())) {
        toastMaker(getString(R.string.invalid_password));
        binding.passwordLoginEditText.setSelection(0);
        return;
      }

      boolean isAdmin = user.getIsAdmin() == 1;

      // Successful authentication: navigate to the landing screen with user context.
      Intent intent = LandingActivity.createIntent(
          LoginActivity.this,
          user.getUsername(),
          user.getUserId(),
          isAdmin);
      startActivity(intent);
    });

  }

  /**
   * Convenience helper for showing short {@link Toast} messages.
   *
   * @param message text to display to the user
   */
  private void toastMaker(String message) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
  }
}
