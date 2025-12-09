package edu.csumb.cst338.otterbots.rockpaperscissors;

import static edu.csumb.cst338.otterbots.rockpaperscissors.LoginActivity.EXTRA_IS_ADMIN;
import static edu.csumb.cst338.otterbots.rockpaperscissors.LoginActivity.EXTRA_USERNAME;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import edu.csumb.cst338.otterbots.rockpaperscissors.database.entities.RockPaperScissorsRepository;
import edu.csumb.cst338.otterbots.rockpaperscissors.database.entities.UserStats;
import edu.csumb.cst338.otterbots.rockpaperscissors.databinding.ActivityLeaderboardBinding;
import edu.csumb.cst338.otterbots.rockpaperscissors.viewHolders.LeaderboardAdapter;
import edu.csumb.cst338.otterbots.rockpaperscissors.viewHolders.LeaderboardViewModel;

/**
 * Description: Show the leaderboard
 * Author: Josh Goldberg
 * Since: 2025.12.07
 */
public class LeaderboardActivity extends AppCompatActivity {
    private RockPaperScissorsRepository repository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityLeaderboardBinding binding = ActivityLeaderboardBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent intent = getIntent();
        String userName = intent.getStringExtra(EXTRA_USERNAME);
        boolean isAdmin = intent.getBooleanExtra(EXTRA_IS_ADMIN, false);

        repository = RockPaperScissorsRepository.getRepository(getApplication());

        LeaderboardViewModel leaderboardViewModel = new ViewModelProvider(this).get(LeaderboardViewModel.class);

        RecyclerView recyclerView = binding.leaderboardRecyclerView;
        final LeaderboardAdapter adapter = new LeaderboardAdapter(new LeaderboardAdapter.LeaderboardDiff());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        leaderboardViewModel.getAllUserStatsByRank().observe(this, adapter::submitList);

        binding.leaderboardViewGoToMainButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = LandingActivity.createIntent(v.getContext(), userName, isAdmin);
                startActivity(intent);
            }
        });
    }

    static Intent leaderboardActivityIntentFactory(Context context, String userName, boolean isAdmin) {
        Intent intent = new Intent(context, LeaderboardActivity.class);
        intent.putExtra(EXTRA_USERNAME, userName);
        intent.putExtra(EXTRA_IS_ADMIN, isAdmin);
        return intent;
    }

    private LiveData<ArrayList<UserStats>> getLeaderboardData() {
        return repository.getAllUserStatsByRank();
    }
}