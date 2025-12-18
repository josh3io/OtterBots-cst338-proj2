package edu.csumb.cst338.otterbots.rockpaperscissors.database.entities;

import android.app.Application;
import androidx.room.Room;
import java.util.concurrent.Executors;

/**
 * extend the RPS repository so we can mock with an in memory database and a single thread executor
 * and allow main thread queries
 */
class TestRepository extends RockPaperScissorsRepository {

  public TestRepository(Application application) {
    // get an in memory db instead of our singleton
    RockPaperScissorsDatabase db = Room.inMemoryDatabaseBuilder(application,
            RockPaperScissorsDatabase.class)
        .setTransactionExecutor(Executors.newSingleThreadExecutor())
        .allowMainThreadQueries()
        .build();

    // and load up the DAOs here.
    this.rpsRoundDAO = db.rpsRoundDAO();
    this.userStatsDAO = db.userStatsDAO();
    this.userJoinUserStatsDAO = db.userJoinUserStatsDAO();
    this.userDAO = db.userDAO();
  }
}
