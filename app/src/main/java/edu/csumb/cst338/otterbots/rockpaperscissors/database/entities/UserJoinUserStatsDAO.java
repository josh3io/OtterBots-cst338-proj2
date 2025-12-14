package edu.csumb.cst338.otterbots.rockpaperscissors.database.entities;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Ignore;
import androidx.room.Query;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Description: DAO interface for User and UserStats join queries
 * Author: Josh Goldberg
 * Since: 2025.12.13
 */

@Dao
public interface UserJoinUserStatsDAO {

    @Query("SELECT u.userId,u.username," +
            "us.id as us_id," +
            "us.userId as us_userId," +
            "us.wins as us_wins," +
            "us.losses as us_losses," +
            "us.`ties` as us_ties," +
            "us.maxStreak as us_maxStreak," +
            "us.currentStreak as us_currentStreak  "
            + "FROM " + RockPaperScissorsDatabase.USER_TABLE + " u "
            + "LEFT JOIN " + RockPaperScissorsDatabase.USER_STATS_TABLE + " us "
            + "ON u.userId = us.userId "
            + "ORDER BY us.wins - us.losses DESC")
    LiveData<List<UserJoinUserStats>> getUsernameAndUserStats();
}
