package edu.csumb.cst338.otterbots.rockpaperscissors;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.LiveData;

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
            username = binding.adminUsernameEditText.getText().toString();
            password = binding.adminPasswordEditText.getText().toString();
            confirmPassword = binding.adminConfirmPasswordEditText.getText().toString();
            if (confirmPassword.equals(password)) {
                LiveData<User> existingUser = repository.getUserByUsername(username);
                if (existingUser != null){
                    toastMaker(username + " " + "IS ALREADY CREATED");
                }
                else {
                    User user = new User(username, confirmPassword, 0);
                    repository.insertUser(user);
                    toastMaker("NEW USER ACCOUNT HAS BEEN CREATED");
                }


            }
            else {
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