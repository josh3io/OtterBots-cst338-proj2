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

    @Query("UPDATE " + RockPaperScissorsDatabase.USER_TABLE +
            " SET username = :newUsername WHERE userId = :userId")
    int updateUsername(int userId, String newUsername);

    @Query("UPDATE " + RockPaperScissorsDatabase.USER_TABLE +
            " SET password = :newPassword WHERE userId = :userId")
    int updatePassword(int userId, String newPassword);

}
