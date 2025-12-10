package edu.csumb.cst338.otterbots.rockpaperscissors.viewHolders;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.ArrayList;

import edu.csumb.cst338.otterbots.rockpaperscissors.RankedUserStats;
import edu.csumb.cst338.otterbots.rockpaperscissors.database.entities.RockPaperScissorsRepository;
import edu.csumb.cst338.otterbots.rockpaperscissors.database.entities.UserStats;

public class LeaderboardViewModel extends AndroidViewModel {
    private final RockPaperScissorsRepository repository;

    public LeaderboardViewModel(Application application) {
        super(application);
        repository = RockPaperScissorsRepository.getRepository(application);
    }

    public LiveData<ArrayList<UserStats>> getAllUserStatsByRank() {
        return repository.getAllUserStatsByRank();
    }

    public void insert(UserStats stats) {
        repository.insertOrUpdateUserStats(stats);
    }
}
