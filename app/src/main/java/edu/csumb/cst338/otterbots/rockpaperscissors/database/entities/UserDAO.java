package edu.csumb.cst338.otterbots.rockpaperscissors.database.entities;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.lifecycle.LiveData;


@Dao
public interface UserDAO {
    @Insert
    long insert(User user);

    @Query("SELECT * FROM " + RockPaperScissorsDatabase.USER_TABLE +
            " WHERE username = :username LIMIT 1")
    LiveData<User> getUserByUsername(String username);

    @Query("SELECT * FROM " + RockPaperScissorsDatabase.USER_TABLE +
            " WHERE username = :username AND password = :password LIMIT 1")
    LiveData<User> getUserLogin(String username, String password);

    @Query("SELECT * FROM " + RockPaperScissorsDatabase.USER_TABLE +
            " WHERE userId = :id LIMIT 1")
    LiveData<User> getUserById(int id);
}
