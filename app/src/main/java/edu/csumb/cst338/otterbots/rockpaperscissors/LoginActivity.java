package edu.csumb.cst338.otterbots.rockpaperscissors;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import edu.csumb.cst338.otterbots.rockpaperscissors.database.entities.RockPaperScissorsRepository;
import edu.csumb.cst338.otterbots.rockpaperscissors.databinding.ActivityLoginBinding;

public class LoginActivity extends AppCompatActivity {
    private ActivityLoginBinding binding;
    private RockPaperScissorsRepository repository;
    public static final String PREFS_NAME = "otterbots_prefs";
    public static final String KEY_LAST_USERNAME = "last_username";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        String saved = sharedPreferences.getString(KEY_LAST_USERNAME, "");
        if (!saved.isEmpty()) {
            binding.userNameLoginEditText.setText(saved);
        }

        repository = RockPaperScissorsRepository.getRepository(getApplication());

        binding.loginButton.setOnClickListener(v -> verifyUser());

        binding.registerTextView.setOnClickListener(v -> startActivity(RegisterActivity.createRegisterIntent(v.getContext())));
    }

    private void verifyUser() {
        String username = binding.userNameLoginEditText.getText().toString().trim();
        if (username.isEmpty()) {
            toastMaker(getString(R.string.error_username_blank));
            return;
        }

        // Save only valid, non-empty usernames.
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        prefs.edit().putString(KEY_LAST_USERNAME, username).apply();

        String userPassword = binding.passwordLoginEditText.getText().toString().trim();
        if (userPassword.isEmpty()) {
            toastMaker(getString(R.string.error_password_blank));
            return;
        }

        // Look up user by username, then validate the password in code
        repository.getUserByUsername(username).observe(this, user -> {
            if (user == null) {
                // No user with that username
                toastMaker(getString(R.string.invalid_username));
                binding.userNameLoginEditText.setSelection(0);
                return;
            }
            if (!userPassword.equals(user.getPassword())) {
                toastMaker(getString(R.string.invalid_password));
                binding.passwordLoginEditText.setSelection(0);
                return;
            }

            boolean isAdmin = user.getIsAdmin() == 1;

            Intent intent = LandingActivity.createIntent(
                    LoginActivity.this,
                    user.getUsername(),
                    user.getUserId(),
                    isAdmin);
            startActivity(intent);
        });

    }

    private void toastMaker(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    public static Intent createLogoutIntent(Context context) {
        Intent logoutIntent = new Intent(context, LoginActivity.class);
        logoutIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        return logoutIntent;
    }
}
