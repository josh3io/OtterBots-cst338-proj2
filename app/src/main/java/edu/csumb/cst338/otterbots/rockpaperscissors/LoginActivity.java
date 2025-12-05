package edu.csumb.cst338.otterbots.rockpaperscissors;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import edu.csumb.cst338.otterbots.rockpaperscissors.database.entities.RockPaperScissorsRepository;
import edu.csumb.cst338.otterbots.rockpaperscissors.databinding.ActivityLoginBinding;

public class LoginActivity extends AppCompatActivity {
    public static final String EXTRA_USERNAME = "userName";
    public static final String EXTRA_IS_ADMIN = "isAdmin";

    private ActivityLoginBinding binding;
    private RockPaperScissorsRepository repository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        repository = RockPaperScissorsRepository.getRepository(getApplication());

        binding.loginButton.setOnClickListener(v -> verifyUser());
    }

    private void verifyUser() {
        String userName = binding.userNameLoginEditText.getText().toString().trim();
        if (userName.isEmpty()) {
            toastMaker("Username should not be blank.");
            return;
        }

        String userPassword = binding.passwordLoginEditText.getText().toString().trim();
        if (userPassword.isEmpty()) {
            toastMaker("Password should not be blank.");
            return;
        }

        //TODO: Replace following code with real DB-based verification once repo is ready.
        Intent intent = new Intent(LoginActivity.this, LandingActivity.class);
        intent.putExtra(EXTRA_USERNAME, userName);

        //TODO: Replace later with real DB isAdmin check.
        boolean isAdmin = userName.equalsIgnoreCase("admin");
        intent.putExtra(EXTRA_IS_ADMIN, isAdmin);

        startActivity(intent);
    }

    private void toastMaker(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
