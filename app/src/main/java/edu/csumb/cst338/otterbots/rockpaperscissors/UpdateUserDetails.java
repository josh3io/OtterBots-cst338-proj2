package edu.csumb.cst338.otterbots.rockpaperscissors;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import edu.csumb.cst338.otterbots.rockpaperscissors.database.entities.RockPaperScissorsRepository;
import edu.csumb.cst338.otterbots.rockpaperscissors.databinding.ActivityUpdateUserDetailsBinding;

public class UpdateUserDetails extends AppCompatActivity {

    private ActivityUpdateUserDetailsBinding binding;
    private RockPaperScissorsRepository repository;
    private int userId;

    public static final String EXTRA_USER_ID = "extra_user_id";

    public static Intent createIntent(Context context, int userId) {
        Intent intent = new Intent(context, UpdateUserDetails.class);
        intent.putExtra(EXTRA_USER_ID, userId);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUpdateUserDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        repository = RockPaperScissorsRepository.getRepository(getApplication());
        userId = getIntent().getIntExtra(EXTRA_USER_ID, -1);
        repository.getUserById(userId).observe(this, user -> {
            if (user != null) {
                binding.CurrentUsername.setText(
                        getString(R.string.current_username) + " " + user.getUsername()
                );
            }
        });

        binding.NewUsername.setText("");
        binding.NewPassword.setText("");
        binding.ConfirmPassword.setText("");

        binding.ReturnButton.setOnClickListener(v -> finish());
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

    private void toastMaker(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}