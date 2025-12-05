package edu.csumb.cst338.otterbots.rockpaperscissors.database.entities;

import android.app.Application;
import android.util.Log;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import edu.csumb.cst338.otterbots.rockpaperscissors.MainActivity;
import edu.csumb.cst338.otterbots.rockpaperscissors.R;

public class RockPaperScissorsRepository {
    private static RockPaperScissorsRepository repository;

    /**
     * Constructor
     * @param application the android application
     */
    private RockPaperScissorsRepository(Application application) {
        RockPaperScissorsDatabase db = RockPaperScissorsDatabase.getDatabase(application);
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
}
