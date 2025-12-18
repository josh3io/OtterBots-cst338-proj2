package edu.csumb.cst338.otterbots.rockpaperscissors.viewHolders.leaderboard;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import edu.csumb.cst338.otterbots.rockpaperscissors.database.entities.RockPaperScissorsRepository;
import edu.csumb.cst338.otterbots.rockpaperscissors.database.entities.UserJoinUserStats;
import edu.csumb.cst338.otterbots.rockpaperscissors.database.entities.UserStats;
import java.util.ArrayList;

 /**
 * Description: View model for the leaderboard
 * Author: Josh Goldberg
 * Since: 2025.12.08
 */
public class LeaderboardViewModel extends AndroidViewModel {

  private final RockPaperScissorsRepository repository;

  public LeaderboardViewModel(Application application) {
    super(application);
    repository = RockPaperScissorsRepository.getRepository(application);
  }

  /**
  * Get the list of all user stats, sorted by leaderboard rank
  *
  * @return live data of the users' gameplay statistics
  */
  public LiveData<ArrayList<UserJoinUserStats>> getAllUserStatsByRank() {
    return repository.getAllUserStatsByRank();
  }

  public void insert(UserStats stats) {
    repository.insertOrUpdateUserStats(stats);
  }
}
