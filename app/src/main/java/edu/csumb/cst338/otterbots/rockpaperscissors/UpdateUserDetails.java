package edu.csumb.cst338.otterbots.rockpaperscissors;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.content.Intent;

import edu.csumb.cst338.otterbots.rockpaperscissors.databinding.ActivityUpdateUserDetailsBinding;

public class UpdateUserDetails extends AppCompatActivity {

    private ActivityUpdateUserDetailsBinding binding;

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

        int userId = getIntent().getIntExtra(EXTRA_USER_ID, -1);
        binding.CurrentUsername.setText("Current Username: (userId=" + userId + ")");

        binding.NewUsername.setText("");
        binding.NewPassword.setText("");
        binding.ConfirmPassword.setText("");

        binding.ReturnButton.setOnClickListener(v -> finish());

    }
}