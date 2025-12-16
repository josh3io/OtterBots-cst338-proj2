package edu.csumb.cst338.otterbots.rockpaperscissors;

import static edu.csumb.cst338.otterbots.rockpaperscissors.LandingActivity.EXTRA_IS_ADMIN;
import static edu.csumb.cst338.otterbots.rockpaperscissors.LandingActivity.EXTRA_USERNAME;
import static edu.csumb.cst338.otterbots.rockpaperscissors.LandingActivity.EXTRA_USER_ID;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import edu.csumb.cst338.otterbots.rockpaperscissors.database.entities.RockPaperScissorsRepository;
import edu.csumb.cst338.otterbots.rockpaperscissors.database.entities.UserJoinUserStats;
import edu.csumb.cst338.otterbots.rockpaperscissors.databinding.ActivityLeaderboardBinding;
import edu.csumb.cst338.otterbots.rockpaperscissors.viewHolders.leaderboard.LeaderboardAdapter;
import edu.csumb.cst338.otterbots.rockpaperscissors.viewHolders.leaderboard.LeaderboardViewModel;
import edu.csumb.cst338.otterbots.rockpaperscissors.viewHolders.leaderboard.RankedUserStats;

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
        String username = intent.getStringExtra(EXTRA_USERNAME);
        int userId = intent.getIntExtra(EXTRA_USER_ID, -1);
        boolean isAdmin = intent.getBooleanExtra(EXTRA_IS_ADMIN, false);

        repository = RockPaperScissorsRepository.getRepository(getApplication());

        LeaderboardViewModel leaderboardViewModel = new ViewModelProvider(this).get(LeaderboardViewModel.class);

        RecyclerView recyclerView = binding.leaderboardRecyclerView;
        final LeaderboardAdapter adapter = new LeaderboardAdapter(new LeaderboardAdapter.LeaderboardDiff());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        leaderboardViewModel.getAllUserStatsByRank().observe(this, userJoinUserStatsList -> {
            List<RankedUserStats> rankedUserStatsList = new ArrayList<RankedUserStats>();
            for (UserJoinUserStats stats : userJoinUserStatsList) {
                rankedUserStatsList.add(
                        RankedUserStats.getRankedUserStats(
                            rankedUserStatsList.size()+1,
                            stats
                        )
                );
            }
            adapter.submitList(rankedUserStatsList);
        });

        binding.leaderboardViewGoToMainButton.setOnClickListener(v -> {
            Intent intent1 = LandingActivity.createIntent(v.getContext(), username, userId, isAdmin);
            startActivity(intent1);
        });

        binding.leaderboardResetStatsButton.setOnClickListener(v -> {
            Intent resetIntent = ResetStats.createIntent(this, userId, username);
            startActivity(resetIntent);
        });
    }

    static Intent leaderboardActivityIntentFactory(Context context, String userName, int userId, boolean isAdmin) {
        Intent intent = new Intent(context, LeaderboardActivity.class);
        intent.putExtra(EXTRA_USERNAME, userName);
        intent.putExtra(EXTRA_USER_ID, userId);
        intent.putExtra(EXTRA_IS_ADMIN, isAdmin);
        return intent;
    }

    private LiveData<ArrayList<UserJoinUserStats>> getLeaderboardData() {
        return repository.getAllUserStatsByRank();
    }
}