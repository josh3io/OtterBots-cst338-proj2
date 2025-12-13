package edu.csumb.cst338.otterbots.rockpaperscissors.database.entities;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import edu.csumb.cst338.otterbots.rockpaperscissors.MainActivity;
import edu.csumb.cst338.otterbots.rockpaperscissors.database.typeConverters.LocalDateTypeConverter;



/**
 * Description: Manage the database for the application
 * Author: Josh Goldberg
 * Since: 2025.12.03
 */

//TODO: add data object entity classes here; remove Dummy

@TypeConverters(LocalDateTypeConverter.class)
@Database(entities = {RpsRound.class, UserStats.class, User.class}, version = 5, exportSchema = false)
public abstract class RockPaperScissorsDatabase extends RoomDatabase {
    private static final String DATABASE_NAME = "RockPaperScissorsDatabase";
    static final String USER_TABLE = "userLogin";
    static final String USER_STATS_TABLE = "userStatsTable";
    static final String RPS_ROUND_TABLE = "rpsRoundTable";

    private static volatile RockPaperScissorsDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;

    static final ExecutorService databaseWriteExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    /**
     * Get the singleton database instance
     * @param context some context that can get the application context
     * @return singleton database instance for the app
     */
    static RockPaperScissorsDatabase getDatabase(final Context context) {
        Log.d(MainActivity.TAG, "Getting database singleton");
        if (INSTANCE == null) {
            // lock for singleton creation
            synchronized (RockPaperScissorsDatabase.class) {
                // we might have been queuing, so check again now that we have the lock
                if (INSTANCE == null) {
                    Log.d(MainActivity.TAG,"Building Room Database");
                    // create the database instance
                    // delete everything and add the default values
                    //     if we're changing the db version
                    INSTANCE = Room.databaseBuilder(
                                    context.getApplicationContext(),
                                    RockPaperScissorsDatabase.class,
                                    DATABASE_NAME
                            ).fallbackToDestructiveMigration(true)
                            .addCallback(addDefaultValues)
                            .build();
                }
            }
        }
        return INSTANCE;
    }


    /**
     * Store default entry values for some users
     */
    private static final RoomDatabase.Callback addDefaultValues = new RoomDatabase.Callback() {
        @Override
        public void onOpen(@NonNull SupportSQLiteDatabase db) {
            super.onOpen(db);
            Log.d(MainActivity.TAG,"Opening Database");
        }

        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            Log.d(MainActivity.TAG, "DATABASE CREATED!");
            databaseWriteExecutor.execute(() -> {
                // Default users for testing LoginActivity
                // TODO: Delete default users later, possibly after RegisterActivity DB integration
                UserDAO userDAO = INSTANCE.userDAO();
                userDAO.insert(new User("admin", "admin", 1));
                userDAO.insert(new User("test", "test", 0));

                Log.i(MainActivity.TAG, "Inserting default data");

                UserStatsDAO userStatsDAO = INSTANCE.userStatsDAO();
                userStatsDAO.insert(new UserStats(1,1,2,3,4,4));
                userStatsDAO.insert(new UserStats(2,2,2,3,4,4));
                userStatsDAO.insert(new UserStats(3,5,2,3,4,4));

            });
        }
    };

    //TODO: add DAOs here

    /**
     * abstract method for the rps round DAO
     * @return DAO for the rps round table
     */
    public abstract RpsRoundDAO rpsRoundDAO();

    /**
     * abstract method for the user stats DAO
     * @return DAO for the user stats table
     */
    public abstract UserStatsDAO userStatsDAO();
    /**
     * abstract method for the user DAO
     * @return DAO for the user table
     */
    public abstract UserDAO userDAO();
}
