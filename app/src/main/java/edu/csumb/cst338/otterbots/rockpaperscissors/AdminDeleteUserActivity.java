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

    public static Intent createIntent(Context context) {
        return new Intent(context, AdminDeleteUserActivity.class);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityAdminDeleteuserAcitivyBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.adminDeleteUserRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new DeleteUserViewAdapter(
                new DeleteUserViewAdapter.DeleteUserDiff()
        );
        binding.adminDeleteUserRecyclerView.setAdapter(adapter);

        viewModel = new ViewModelProvider(this).get(DeleteUserViewModel.class);
        repository = RockPaperScissorsRepository.getRepository(getApplication());

        viewModel.getAllUsers().observe(this, users -> adapter.submitList(users));

        binding.returnToMainTextView.setOnClickListener(v -> finish());

        binding.deleteUserButton.setOnClickListener(v -> {
            String username = binding.usernameEditTextView.getText().toString().trim();
            String confirm = binding.confirmUsernameEditTextView.getText().toString().trim();

            if (username.isEmpty() || confirm.isEmpty()) {
                toastMaker("Please fill in both username fields");
                return;
            }

            if (!username.equals(confirm)) {
                toastMaker("Usernames do not match");
                return;
            }

            repository.getUserByUsername(username).observe(this, user -> {
                if (user == null) {
                    toastMaker("User does not exist");
                } else {
                    viewModel.deleteUserByUsername(username);
                    toastMaker("User deleted");
                }
                binding.usernameEditTextView.setText("");
                binding.confirmUsernameEditTextView.setText("");

                repository.getUserByUsername(username).removeObservers(this);
            });
        });
    }

    private void toastMaker(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
