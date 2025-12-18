package edu.csumb.cst338.otterbots.rockpaperscissors.database.entities;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;
import java.util.List;

/**
 * Description: DAO interface for RPS Round database records Author: Josh Goldberg Since:
 * 2025.12.06
 */

@Dao
public interface RpsRoundDAO {

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  void insert(RpsRound round);

  @Update
  void update(RpsRound round);

  @Delete
  void delete(RpsRound round);

  /**
   * Get all rounds for a userStatsId session
   *
   * @return list of all records
   */
  @Query("SELECT * FROM " + RockPaperScissorsDatabase.RPS_ROUND_TABLE
      + " WHERE userStatsId = :userStatsId ORDER BY date DESC")
  LiveData<List<RpsRound>> getAllRoundsByUserStatsId(int userStatsId);

  @Query("DELETE FROM " + RockPaperScissorsDatabase.RPS_ROUND_TABLE +
      " WHERE userStatsId = :userStatsId")
  void deleteRoundsByUserStatsId(int userStatsId);

}
