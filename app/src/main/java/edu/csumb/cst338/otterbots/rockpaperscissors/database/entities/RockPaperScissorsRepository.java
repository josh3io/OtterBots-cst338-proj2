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
import edu.csumb.cst338.otterbots.rockpaperscissors.R;

public class RockPaperScissorsRepository {
    private static RockPaperScissorsRepository repository;

    private RpsRoundDAO rpsRoundDAO;

    /**
     * Constructor
     * @param application the android application
     */
    private RockPaperScissorsRepository(Application application) {
        RockPaperScissorsDatabase db = RockPaperScissorsDatabase.getDatabase(application);
        this.rpsRoundDAO = db.rpsRoundDAO();
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
}
