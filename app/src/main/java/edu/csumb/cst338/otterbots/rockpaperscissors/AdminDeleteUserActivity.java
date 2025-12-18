/**
 * AdminDeleteUserActivity
 * ------------------------
 * Activity that allows an admin to view all users and delete a user
 * by confirming their username. Uses MVVM architecture with a ViewModel,
 * Repository, and RecyclerView adapter to display user data.
 *
 * Author: Christopher Buenrostro
 */
package edu.csumb.cst338.otterbots.rockpaperscissors;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import edu.csumb.cst338.otterbots.rockpaperscissors.database.entities.RockPaperScissorsRepository;
import edu.csumb.cst338.otterbots.rockpaperscissors.databinding.ActivityAdminDeleteuserAcitivyBinding;
import edu.csumb.cst338.otterbots.rockpaperscissors.viewHolders.deleteUser.DeleteUserViewAdapter;
import edu.csumb.cst338.otterbots.rockpaperscissors.viewHolders.deleteUser.DeleteUserViewModel;

public class AdminDeleteUserActivity extends AppCompatActivity {

    private DeleteUserViewAdapter adapter;
    private ActivityAdminDeleteuserAcitivyBinding binding;
    private DeleteUserViewModel viewModel;
    private RockPaperScissorsRepository repository;

    /**
     * Helper method to create an Intent for launching this activity.
     */
    public static Intent createIntent(Context context) {
        return new Intent(context, AdminDeleteUserActivity.class);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Inflate view binding for UI access
        binding = ActivityAdminDeleteuserAcitivyBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Setup RecyclerView layout manager
        binding.adminDeleteUserRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Adapter setup using DiffUtil for efficient updates
        adapter = new DeleteUserViewAdapter(new DeleteUserViewAdapter.DeleteUserDiff());
        binding.adminDeleteUserRecyclerView.setAdapter(adapter);

        // Get ViewModel and Repository instances
        viewModel = new ViewModelProvider(this).get(DeleteUserViewModel.class);
        repository = RockPaperScissorsRepository.getRepository(getApplication());

        // Observe user list and update adapter whenever data changes
        viewModel.getAllUsers().observe(this, users -> adapter.submitList(users));

        // Return to previous screen
        binding.returnToMainTextView.setOnClickListener(v -> finish());

        /**
         * Handles delete user button logic:
         * - Validates username inputs
         * - Ensures both fields match
         * - Checks if the user exists before deleting
         * - Clears fields and shows feedback via Toast
         */
        binding.deleteUserButton.setOnClickListener(v -> {
            String username = binding.usernameEditTextView.getText().toString().trim();
            String confirm = binding.confirmUsernameEditTextView.getText().toString().trim();

            // Validate input fields
            if (username.isEmpty() || confirm.isEmpty()) {
                toastMaker("Please fill in both username fields");
                return;
            }

            // Validate username confirmation
            if (!username.equals(confirm)) {
                toastMaker("Usernames do not match");
                return;
            }

            // Check if user exists before deletion
            repository.getUserByUsername(username).observe(this, user -> {
                if (user == null) {
                    toastMaker("User does not exist");
                } else {
                    viewModel.deleteUserByUsername(username);
                    toastMaker("User deleted");
                }

                // Clear text fields after action
                binding.usernameEditTextView.setText("");
                binding.confirmUsernameEditTextView.setText("");

                // Remove observer to prevent LiveData repeating calls
                repository.getUserByUsername(username).removeObservers(this);
            });
        });
    }

    /**
     * Helper method to show short Toast messages.
     */
    private void toastMaker(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
