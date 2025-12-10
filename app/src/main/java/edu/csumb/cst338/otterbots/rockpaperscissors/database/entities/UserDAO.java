package edu.csumb.cst338.otterbots.rockpaperscissors.database.entities;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

@Dao
public interface UserDAO {
    @Insert
    long insert(User user);

    @Query("SELECT * FROM " + RockPaperScissorsDatabase.USER_TABLE +
            " WHERE userName = :username LIMIT 1")
    User getUserByUsername(String username);

    @Query("SELECT * FROM " + RockPaperScissorsDatabase.USER_TABLE +
            " WHERE userName = :username AND password = :password LIMIT 1")
    User getUserLogin(String username, String password);

    @Query("SELECT * FROM " + RockPaperScissorsDatabase.USER_TABLE +
            " WHERE userId = :id LIMIT 1")
    User getUserById(int id);
}
