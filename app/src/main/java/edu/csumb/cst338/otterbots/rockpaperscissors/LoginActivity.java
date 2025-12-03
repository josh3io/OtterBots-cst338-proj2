package edu.csumb.cst338.otterbots.rockpaperscissors;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import edu.csumb.cst338.otterbots.rockpaperscissors.databinding.ActivityLoginBinding;

public class LoginActivity extends AppCompatActivity {

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

        //TODO: Replace following code with real DB-based verification once repo is ready.
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        intent.putExtra("testUser1", userName);
        startActivity(intent);

    }

    private void toastMaker(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
