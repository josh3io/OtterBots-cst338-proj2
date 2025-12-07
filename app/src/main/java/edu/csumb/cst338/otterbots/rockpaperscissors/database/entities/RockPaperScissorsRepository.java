package edu.csumb.cst338.otterbots.rockpaperscissors.database.entities;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;

import java.util.ArrayList;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import edu.csumb.cst338.otterbots.rockpaperscissors.MainActivity;

public class RockPaperScissorsRepository {
    private static RockPaperScissorsRepository repository;

    protected RpsRoundDAO rpsRoundDAO;
    protected UserStatsDAO userStatsDAO;

    /**
     * Constructor
     * @param application the android application
     */
    private RockPaperScissorsRepository(Application application) {
        RockPaperScissorsDatabase db = RockPaperScissorsDatabase.getDatabase(application);
        this.rpsRoundDAO = db.rpsRoundDAO();
        this.userStatsDAO = db.userStatsDAO();
    }

    public RockPaperScissorsRepository() {
    }

    /**
     * Factory method to instantiate the repository and db in a separate thread
     * @param application the android application
     * @return a RockPaperScissorsRepository object that abstracts the db
     */
    public static RockPaperScissorsRepository getRepository(Application application) {
        if (repository != null) {
            return repository;
        }

        Future<RockPaperScissorsRepository> future = RockPaperScissorsDatabase.databaseWriteExecutor.submit(
                new Callable<RockPaperScissorsRepository>() {
                    @Override
                    public RockPaperScissorsRepository call() throws Exception {
                        repository = new RockPaperScissorsRepository(application);
                        return repository;
                    }
                }
        );

        try {
            return future.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            Log.i(MainActivity.TAG,"Thread Error getting the RPS Repository.");
        }

        return null;
    }

    //TODO: add DAO wrapper methods here; they should all use LiveData<Object> return types

    /**
     * Add a new round to the database
     * @param round the round entity to record
     */
    public void insertRound(RpsRound round) {
        RockPaperScissorsDatabase.databaseWriteExecutor.execute(() -> {
            rpsRoundDAO.insert(round);
        });
    }

    /**
     * Get all rps rounds for a userStatsId
     * @param userStatsId the userStatsId id to use for looking up round records
     * @return list of records or null ArrayList if none are found
     */
    public LiveData<ArrayList<RpsRound>> getAllUserStatsIdRounds(int userStatsId) {
        return Transformations.map(rpsRoundDAO.getAllRoundsByUserStatsId(userStatsId), list -> {
            if (list == null) {
                return new ArrayList<>();
            }
            return new ArrayList<>(list);
        });
    }

    /**
     * Get all user stats for the leaderboard
     * @return list of all user stats ordered by wins - losses
     */
    public LiveData<ArrayList<UserStats>> getAllUserStatsByRank() {
        return Transformations.map(userStatsDAO.getAllUserStatsByRank(), list -> {
            if (list == null) {
                return new ArrayList<>();
            }
            return new ArrayList<>(list);
        });
    }
}
