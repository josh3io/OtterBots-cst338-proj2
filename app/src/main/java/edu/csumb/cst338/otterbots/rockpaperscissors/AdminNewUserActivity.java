package edu.csumb.cst338.otterbots.rockpaperscissors;

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

  private String username = "";
  private String password = "";
  private String confirmPassword = "";

  public static Intent createIntent(Context context) {
    return new Intent(context, AdminNewUserActivity.class);
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    ActivityAdminNewUserBinding binding = ActivityAdminNewUserBinding.inflate(getLayoutInflater());
    setContentView(binding.getRoot());

    repository = RockPaperScissorsRepository.getRepository(getApplication());
    setupUserUI(binding);
  }

  private void setupUserUI(ActivityAdminNewUserBinding binding) {

    binding.addUserButton.setOnClickListener(v -> {
      username = binding.adminUsernameEditText.getText().toString().trim();
      password = binding.adminPasswordEditText.getText().toString().trim();
      confirmPassword = binding.adminConfirmPasswordEditText.getText().toString().trim();

      // Check for empty fields FIRST
      if (username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
        toastMaker("All fields are required");
        return;
      }

      // Then check if passwords match
      if (confirmPassword.equals(password)) {

        LiveData<User> userLiveData = repository.getUserByUsername(username);

        // One-shot observer: handle first value, then detach
        Observer<User> observer = new Observer<User>() {
          @Override
          public void onChanged(User user) {
            if (user != null) {
              // Username is taken
              toastMaker(username + " IS ALREADY CREATED");
            } else {
              // Username is free -> insert user
              User newUser = new User(username, password, 0); // 0 = non-admin
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

    binding.addAdminButton.setOnClickListener(v -> {
      username = binding.adminUsernameEditText.getText().toString().trim();
      password = binding.adminPasswordEditText.getText().toString().trim();
      confirmPassword = binding.adminConfirmPasswordEditText.getText().toString().trim();

      // Check for empty fields FIRST
      if (username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
        toastMaker("All fields are required");
        return;
      }

      // Then check if passwords match
      if (confirmPassword.equals(password)) {

        LiveData<User> userLiveData = repository.getUserByUsername(username);

        // One-shot observer: handle first value, then detach
        Observer<User> observer = new Observer<User>() {
          @Override
          public void onChanged(User user) {
            if (user != null) {
              toastMaker(username + " IS ALREADY CREATED");
            } else {
              User newAdmin = new User(username, password, 1); // 1 = admin
              repository.insertUser(newAdmin);
              toastMaker("NEW ADMIN ACCOUNT HAS BEEN CREATED");
            }
            userLiveData.removeObserver(this);
          }
        };
        userLiveData.observe(AdminNewUserActivity.this, observer);
      } else {
        toastMaker(
            "CONFIRM PASSWORD DOES NOT MATCH");
      }
    });

    binding.returnSelectableTextView.setOnClickListener(v -> finish());
  }

  private void toastMaker(String message) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
  }
}