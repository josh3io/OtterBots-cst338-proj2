package edu.csumb.cst338.otterbots.rockpaperscissors.viewHolders.gameHistory;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.ArrayList;

import edu.csumb.cst338.otterbots.rockpaperscissors.database.entities.RockPaperScissorsRepository;
import edu.csumb.cst338.otterbots.rockpaperscissors.database.entities.RpsRound;
import edu.csumb.cst338.otterbots.rockpaperscissors.database.entities.UserStats;

public class GameHistoryViewModel extends AndroidViewModel {
    private final RockPaperScissorsRepository repository;

    public GameHistoryViewModel(Application application) {
        super(application);
        repository = RockPaperScissorsRepository.getRepository(application);
    }

    public LiveData<ArrayList<RpsRound>> getAllRoundsByUser(int userStatsId) {
        return repository.getAllUserStatsIdRounds(userStatsId);
    }
}
