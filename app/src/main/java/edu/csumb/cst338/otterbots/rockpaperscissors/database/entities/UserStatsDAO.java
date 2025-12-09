package edu.csumb.cst338.otterbots.rockpaperscissors.database.entities;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

/**
 * Description: DAO interface for user stats database records
 * Author: Josh Goldberg
 * Since: 2025.12.06
 */

@Dao
public interface UserStatsDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(UserStats stats);

    /**
     * Get stats record for a user
     * @param userId user id to get stats for
     * @return the user stats record for the given user
     */
    @Query("SELECT * FROM " + RockPaperScissorsDatabase.USER_STATS_TABLE + " WHERE userId = :userId")
    LiveData<UserStats> getUserStatsByUserId(int userId);

    @Query("SELECT * FROM " + RockPaperScissorsDatabase.USER_STATS_TABLE + " ORDER BY wins - losses DESC")
    LiveData<List<UserStats>> getAllUserStatsByRank();
}
