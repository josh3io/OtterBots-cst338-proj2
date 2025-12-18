package edu.csumb.cst338.otterbots.rockpaperscissors;

/**
 * AdminNewUserActivity
 * ---------------------
 * Activity that enables an administrator to create new user accounts or admin accounts.
 * The activity validates username/password fields, checks the database for duplicates,
 * and inserts either a normal user or an admin based on which button is pressed.
 *
 * Author: Christopher Buenrostro
 */
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

import edu.csumb.cst338.otterbots.rockpaperscissors.database.entities.RockPaperScissorsRepository;
import edu.csumb.cst338.otterbots.rockpaperscissors.database.entities.User;
import edu.csumb.cst338.otterbots.rockpaperscissors.databinding.ActivityAdminNewUserBinding;

public class AdminNewUserActivity extends AppCompatActivity {

  private RockPaperScissorsRepository repository;

  // Fields used to store user input temporarily
  private String username = "";
  private String password = "";
  private String confirmPassword = "";

  /**
   * Helper method for starting this activity from elsewhere.
   */
  public static Intent createIntent(Context context) {
    return new Intent(context, AdminNewUserActivity.class);
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    ActivityAdminNewUserBinding binding =
        ActivityAdminNewUserBinding.inflate(getLayoutInflater());
    setContentView(binding.getRoot());

    // Initialize repository for DB operations
    repository = RockPaperScissorsRepository.getRepository(getApplication());

    // Configure event listeners and UI logic
    setupUserUI(binding);
  }

  /**
   * Sets up all button listeners and user/admin creation logic.
   */
  private void setupUserUI(ActivityAdminNewUserBinding binding) {

    /**
     * "Create User" button logic
     * ---------------------------
     * Validates non-empty fields → validates password match →
     * checks database for username availability using a one-shot observer →
     * inserts non-admin user if username is free.
     */
    binding.addUserButton.setOnClickListener(v -> {
      username = binding.adminUsernameEditText.getText().toString().trim();
      password = binding.adminPasswordEditText.getText().toString().trim();
      confirmPassword = binding.adminConfirmPasswordEditText.getText().toString().trim();

      // Require all fields
      if (username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
        toastMaker("All fields are required");
        return;
      }

      // Validate passwords match
      if (confirmPassword.equals(password)) {
        LiveData<User> userLiveData = repository.getUserByUsername(username);

        // One-shot observer: handle first value, then detach
        Observer<User> observer =
            new Observer<User>() {
              @Override
              public void onChanged(User user) {
                if (user != null) {
                  // Username already exists in DB
                  toastMaker(username + " IS ALREADY CREATED");
                } else {
                  // Insert a standard user (isAdmin = 0)
                  User newUser = new User(username, password, 0);
                  repository.insertUser(newUser);
                  toastMaker("NEW USER ACCOUNT HAS BEEN CREATED");
                }
                userLiveData.removeObserver(this);
              }
            };

        userLiveData.observe(AdminNewUserActivity.this, observer);
      } else {
        toastMaker("CONFIRM PASSWORD DOES NOT MATCH");
      }
    });

    /**
     * "Create Admin" button logic
     * ----------------------------
     * Same validation process as user creation, but inserts an admin account instead.
     */
    binding.addAdminButton.setOnClickListener(v -> {
      username = binding.adminUsernameEditText.getText().toString().trim();
      password = binding.adminPasswordEditText.getText().toString().trim();
      confirmPassword = binding.adminConfirmPasswordEditText.getText().toString().trim();

      // Require all fields
      if (username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
        toastMaker("All fields are required");
        return;
      }

      // Validate passwords match
      if (confirmPassword.equals(password)) {
        LiveData<User> userLiveData = repository.getUserByUsername(username);

        // One-shot observer: handle first value, then detach
        Observer<User> observer =
            new Observer<User>() {
              @Override
              public void onChanged(User user) {
                if (user != null) {
                  toastMaker(username + " IS ALREADY CREATED");
                } else {
                  // Insert admin user (isAdmin = 1)
                  User newAdmin = new User(username, password, 1);
                  repository.insertUser(newAdmin);
                  toastMaker("NEW ADMIN ACCOUNT HAS BEEN CREATED");
                }
                userLiveData.removeObserver(this);
              }
            };

        userLiveData.observe(AdminNewUserActivity.this, observer);
      } else {
        toastMaker("CONFIRM PASSWORD DOES NOT MATCH");
      }
    });

    // Return to previous screen
    binding.returnSelectableTextView.setOnClickListener(v -> finish());
  }

  /**
   * Utility method for displaying short Toast messages.
   */
  private void toastMaker(String message) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
  }
}