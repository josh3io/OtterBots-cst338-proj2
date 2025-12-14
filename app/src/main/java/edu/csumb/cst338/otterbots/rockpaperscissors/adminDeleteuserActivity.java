package edu.csumb.cst338.otterbots.rockpaperscissors;

import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import edu.csumb.cst338.otterbots.rockpaperscissors.databinding.ActivityAdminDeleteuserAcitivyBinding;
import edu.csumb.cst338.otterbots.rockpaperscissors.viewHolders.DeleteUserViewAdapter;
import edu.csumb.cst338.otterbots.rockpaperscissors.viewHolders.DeleteUserViewModel;

public class adminDeleteuserActivity extends AppCompatActivity {

    private DeleteUserViewAdapter adapter;
    private ActivityAdminDeleteuserAcitivyBinding binding;
    private DeleteUserViewModel viewModel;

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

        viewModel.getAllUsers().observe(this, users -> {
            adapter.submitList(users);
        });

        binding.returnToMainTextView.setOnClickListener(v -> finish());

        binding.deleteUserButton.setOnClickListener(v -> {
            String username = binding.usernameEditTextView.getText().toString().trim();
            String confirm = binding.confirmUsernameEditTextView.getText().toString().trim();

            if (username.isEmpty() || confirm.isEmpty()) {
                Toast.makeText(this, "Please fill in both username fields", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!username.equals(confirm)) {
                Toast.makeText(this, "Usernames do not match", Toast.LENGTH_SHORT).show();
                return;
            }

            viewModel.deleteUserByUsername(username);
            Toast.makeText(this, "Delete requested for user: " + username, Toast.LENGTH_SHORT).show();

            binding.usernameEditTextView.setText("");
            binding.confirmUsernameEditTextView.setText("");
        });
    }
}
