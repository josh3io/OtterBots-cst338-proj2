package edu.csumb.cst338.otterbots.rockpaperscissors.viewHolders.leaderboard;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.ArrayList;

import edu.csumb.cst338.otterbots.rockpaperscissors.database.entities.RockPaperScissorsRepository;
import edu.csumb.cst338.otterbots.rockpaperscissors.database.entities.UserJoinUserStats;
import edu.csumb.cst338.otterbots.rockpaperscissors.database.entities.UserStats;

public class LeaderboardViewModel extends AndroidViewModel {
    private final RockPaperScissorsRepository repository;

    public LeaderboardViewModel(Application application) {
        super(application);
        repository = RockPaperScissorsRepository.getRepository(application);
    }

    public LiveData<ArrayList<UserJoinUserStats>> getAllUserStatsByRank() {
        return repository.getAllUserStatsByRank();
    }

    public void insert(UserStats stats) {
        repository.insertOrUpdateUserStats(stats);
    }
}
