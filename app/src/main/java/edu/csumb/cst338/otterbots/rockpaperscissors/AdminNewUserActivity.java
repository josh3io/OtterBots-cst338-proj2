package edu.csumb.cst338.otterbots.rockpaperscissors;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import edu.csumb.cst338.otterbots.rockpaperscissors.database.entities.RockPaperScissorsRepository;
import edu.csumb.cst338.otterbots.rockpaperscissors.database.entities.User;
import edu.csumb.cst338.otterbots.rockpaperscissors.databinding.ActivityAdminNewUserBinding;

public class AdminNewUserActivity extends AppCompatActivity {
    private RockPaperScissorsRepository repository;

    private String username = "";
    private String password = "";
    private String confirmPassword = "";

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
            if (confirmPassword.equals(password)) {

                // Ask DB if this username already exists
                repository.getUserByUsername(username).observe(this, user -> {

                    // LiveData callback runs each time the db value changes
                    if (user != null) {
                        // Username is taken
                        toastMaker(username + " IS ALREADY CREATED");
                    } else {
                        // Username is free -> insert user
                        User newUser = new User(username, password, 0); // 0 = non-admin
                        repository.insertUser(newUser);
                        toastMaker("NEW USER ACCOUNT HAS BEEN CREATED");
                    }
                });
            } else {
                toastMaker(
                        "CONFIRM PASSWORD DOES NOT MATCH");
            }
        });

        binding.addAdminButton.setOnClickListener(v -> {
            username = binding.adminUsernameEditText.getText().toString().trim();
            password = binding.adminPasswordEditText.getText().toString().trim();
            confirmPassword = binding.adminConfirmPasswordEditText.getText().toString().trim();
            if (confirmPassword.equals(password)) {

                // Ask DB if this username already exists
                repository.getUserByUsername(username).observe(this, user -> {

                    // LiveData callback runs each time the db value changes
                    if (user != null) {
                        // Username is taken
                        toastMaker(username + " IS ALREADY CREATED");
                    } else {
                        // Username is free -> insert user
                        User newAdmin = new User(username, password, 1); // 1 = admin
                        repository.insertUser(newAdmin);
                        toastMaker("NEW ADMIN ACCOUNT HAS BEEN CREATED");
                    }
                });
            } else {
                toastMaker(
                        "CONFIRM PASSWORD DOES NOT MATCH");
            }
        });

        binding.returnSelectableTextView.setOnClickListener(v -> {
            finish();
        });
    }

    private void toastMaker(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}