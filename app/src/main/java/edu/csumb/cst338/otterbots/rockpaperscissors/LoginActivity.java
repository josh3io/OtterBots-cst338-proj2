package edu.csumb.cst338.otterbots.rockpaperscissors;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import edu.csumb.cst338.otterbots.rockpaperscissors.databinding.ActivityLoginBinding;

public class LoginActivity extends AppCompatActivity {
    public static final String EXTRA_USERNAME = "userName";
    public static final String EXTRA_IS_ADMIN = "isAdmin";

    private ActivityLoginBinding binding;
    //TODO: Add rps repository declaration here, once repo is implemented.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //TODO: Add repository initialization here, once repo is implemented.

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

        //TODO: Replace hardcoded login + admin validation with real repo/DB verification once User DAO is implemented.
        boolean isAdmin = userName.equalsIgnoreCase("admin");
        Intent intent = LandingActivity.createIntent(LoginActivity.this, userName, isAdmin);
        startActivity(intent);
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
