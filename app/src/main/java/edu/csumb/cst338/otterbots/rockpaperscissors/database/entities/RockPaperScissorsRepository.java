package edu.csumb.cst338.otterbots.rockpaperscissors.database.entities;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import edu.csumb.cst338.otterbots.rockpaperscissors.MainActivity;


public class RockPaperScissorsRepository {
    private static RockPaperScissorsRepository repository;

    protected RpsRoundDAO rpsRoundDAO;
    protected UserStatsDAO userStatsDAO;
    protected UserDAO userDAO;

    protected UserJoinUserStatsDAO userJoinUserStatsDAO;

    /**
     * Constructor
     *
     * @param application the android application
     */
    private RockPaperScissorsRepository(Application application) {
        Log.d(MainActivity.TAG, "Instantiating Repository");
        RockPaperScissorsDatabase db = RockPaperScissorsDatabase.getDatabase(application);
        this.rpsRoundDAO = db.rpsRoundDAO();
        this.userStatsDAO = db.userStatsDAO();
        this.userDAO = db.userDAO();
        this.userJoinUserStatsDAO = db.userJoinUserStatsDAO();
    }

    public RockPaperScissorsRepository() {
    }

    /**
     * Factory method to instantiate the repository and db in a separate thread
     *
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
            Log.i(MainActivity.TAG, "Thread Error getting the RPS Repository.");
        }

        return null;
    }

    //TODO: add DAO wrapper methods here; they should all use LiveData<Object> return types

    /**
     * Add a new round to the database
     *
     * @param round the round entity to record
     */
    public void insertRound(RpsRound round) {
        RockPaperScissorsDatabase.databaseWriteExecutor.execute(() -> {
            rpsRoundDAO.insert(round);
        });
    }

    /**
     * Get all rps rounds for a userStatsId
     *
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
     *
     * @return list of all user stats ordered by wins - losses
     */
    public LiveData<ArrayList<UserJoinUserStats>> getAllUserStatsByRank() {
        return Transformations.map(userJoinUserStatsDAO.getUsernameAndUserStats(), list -> {
            Log.d(MainActivity.TAG,"List of stats "+list.toString());
            if (list == null) {
                return new ArrayList<>();
            }
            return new ArrayList<>(list);
        });
    }

    /**
     * Add or update a userStats record
     */
    public void insertOrUpdateUserStats(UserStats stats) {

        RockPaperScissorsDatabase.databaseWriteExecutor.execute(() -> {
            userStatsDAO.insert(stats);
        });
    }

    /**
     * Get the stats for a single user
     *
     * @param userId the user to get stats for
     * @return a record of userStats or null
     */
    public LiveData<UserStats> getUserStatsByUserId(int userId) {
        return userStatsDAO.getUserStatsByUserId(userId);
    }

    /**
     * Get a user by username
     *
     * @param username the username to look up
     * @return the user record or null
     */
    public LiveData<User> getUserByUsername(String username) {
        return userDAO.getUserByUsername(username);
    }

    /**
     * Get a user by username and password for login
     *
     * @param username the username to look up
     * @param password the password to look up
     * @return the user record or null
     */
    public LiveData<User> getUserLogin(String username, String password) {
        return userDAO.getUserLogin(username, password);
    }

    /**
     * Insert a new user record
     *
     * @param user the user entity to insert
     */
    public int insertUser(User user) {
        Future<Integer> future = RockPaperScissorsDatabase.databaseWriteExecutor.submit(
                new Callable<Integer>() {
                    @Override
                    public Integer call() throws Exception {
                        return (int) userDAO.insert(user);
                    }
                }
        );
        try {
            return future.get();
        } catch (InterruptedException | ExecutionException e) {
            Log.e(MainActivity.TAG, "Failed to insert new user record. thread error.");
            return -1;
        }
    }

    public LiveData<List<User>> getAllUsers() {
        return userDAO.getAllUsers();
    }

    public void deleteUserByUsername(String username) {
        RockPaperScissorsDatabase.databaseWriteExecutor.execute(() -> {
            userDAO.deleteUserByUsername(username);
        });
    }

}
