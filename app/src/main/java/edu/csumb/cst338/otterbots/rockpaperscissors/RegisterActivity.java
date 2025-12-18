package edu.csumb.cst338.otterbots.rockpaperscissors;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import edu.csumb.cst338.otterbots.rockpaperscissors.database.entities.RockPaperScissorsRepository;
import edu.csumb.cst338.otterbots.rockpaperscissors.database.entities.User;
import edu.csumb.cst338.otterbots.rockpaperscissors.databinding.ActivityRegisterBinding;

/**
 * Description: Activity for a new user to register an account Author: Josh Goldberg Since:
 * 2025.12.09
 */
public class RegisterActivity extends AppCompatActivity {

  private ActivityRegisterBinding binding;

  private RockPaperScissorsRepository repository;

  /**
   * Generate an intent to start this activity
   *
   * @param context source context
   * @return an intent for the register activity
   */
  public static Intent createRegisterIntent(Context context) {
    return new Intent(context, RegisterActivity.class);
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    binding = ActivityRegisterBinding.inflate(getLayoutInflater());
    setContentView(binding.getRoot());

    repository = RockPaperScissorsRepository.getRepository(getApplication());

    binding.registerButton.setOnClickListener(v -> registerUser());

    binding.backToLoginTextView.setOnClickListener(
        v -> startActivity(LoginActivity.createLogoutIntent(v.getContext())));
  }

  /**
   * Register a new user based on the activity input
   */
  private void registerUser() {
    // username can't be blank
    String username = binding.userNameRegisterEditText.getText().toString().trim();
    if (username.isEmpty()) {
      toastMaker(getString(R.string.username_must_not_be_blank));
      return;
    }

    // password can't be blank
    String password1 = binding.passwordRegisterEditText.getText().toString();
    if (password1.isEmpty()) {
      toastMaker(getString(R.string.password_must_not_be_blank));
      return;
    }
    String password2 = binding.passwordConfirmRegisterEditText.getText().toString();

    // password confirmation must equal the password so we know the user knows their password
    // at least for now.
    if (password1.equals(password2)) {
      // Only persist username after all register validations pass
      SharedPreferences prefs = getSharedPreferences(LoginActivity.PREFS_NAME, MODE_PRIVATE);
      prefs.edit().putString(LoginActivity.KEY_LAST_USERNAME, username).apply();

      User user = new User(username, password1, 0);
      int userId = repository.insertUser(user);
      user.setUserId(userId);

      Intent intent = LandingActivity.createIntent(RegisterActivity.this, username,
          user.getUserId(), false);
      startActivity(intent);
    } else {
      toastMaker(getString(R.string.password_confirmation_must_match));
      return;
    }

  }

  /**
   * Easy peasy toasty squeezy
   *
   * @param message the message to show in the toast modal
   */
  private void toastMaker(String message) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
  }
}