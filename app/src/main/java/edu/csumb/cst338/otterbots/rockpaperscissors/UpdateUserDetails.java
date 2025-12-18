package edu.csumb.cst338.otterbots.rockpaperscissors;
/**
 * Description: Activity for updating user details such as username and password.
 * Author: Murtaza Badri
 * Since: 2025.12.12
 */

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import edu.csumb.cst338.otterbots.rockpaperscissors.database.entities.RockPaperScissorsRepository;
import edu.csumb.cst338.otterbots.rockpaperscissors.databinding.ActivityUpdateUserDetailsBinding;

public class UpdateUserDetails extends AppCompatActivity {

  // Intent extra key for passing the logged-in user ID
  public static final String EXTRA_USER_ID = "extra_user_id";
  private ActivityUpdateUserDetailsBinding binding;
  private RockPaperScissorsRepository repository;
  private int userId;

  //Intent factory for launching UpdateUserDetails with a userId
  public static Intent createIntent(Context context, int userId) {
    Intent intent = new Intent(context, UpdateUserDetails.class);
    intent.putExtra(EXTRA_USER_ID, userId);
    return intent;
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    // Set up ViewBinding
    binding = ActivityUpdateUserDetailsBinding.inflate(getLayoutInflater());
    setContentView(binding.getRoot());

    // Initialize repository and retrieve userId
    repository = RockPaperScissorsRepository.getRepository(getApplication());
    userId = getIntent().getIntExtra(EXTRA_USER_ID, -1);

    // Load and display the current username
    repository.getUserById(userId).observe(this, user -> {
      if (user != null) {
        binding.CurrentUsername.setText(
            getString(R.string.current_username) + " " + user.getUsername()
        );
      }
    });

    // Ensure all input fields start empty
    binding.NewUsername.setText("");
    binding.NewPassword.setText("");
    binding.ConfirmPassword.setText("");

    // Return to previous screen without saving
    binding.ReturnButton.setOnClickListener(v -> finish());

    // Handle update button click
    binding.UpdateButton.setOnClickListener(v -> {
      String newUsername = binding.NewUsername.getText().toString().trim();
      String newPassword = binding.NewPassword.getText().toString().trim();
      String confirmPassword = binding.ConfirmPassword.getText().toString().trim();
      if (newUsername.isEmpty()) {
        toastMaker("Username cannot be blank");
        return;
      }
      if (newPassword.isEmpty()) {
        toastMaker("Password cannot be blank");
        return;
      }
      if (!newPassword.equals(confirmPassword)) {
        toastMaker("Passwords do not match");
        return;
      }

      // Update username and password in database
      repository.updateUsername(userId, newUsername);
      repository.updatePassword(userId, newPassword);

      // Update login persistence so the new username is remembered
      getSharedPreferences(LoginActivity.PREFS_NAME, MODE_PRIVATE)
          .edit()
          .putString(LoginActivity.KEY_LAST_USERNAME, newUsername)
          .apply();

      toastMaker("User details updated");
      finish();
    });

  }

//Helper method for displaying short messages to the user
  private void toastMaker(String message) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
  }
}