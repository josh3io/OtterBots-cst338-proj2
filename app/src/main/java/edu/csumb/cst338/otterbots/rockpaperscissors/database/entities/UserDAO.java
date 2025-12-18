package edu.csumb.cst338.otterbots.rockpaperscissors.database.entities;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import java.util.List;

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

  // Returns a LiveData stream of all users stored in the database.
  // Room automatically updates the LiveData when the underlying table changes.
  @Query("SELECT * FROM " + RockPaperScissorsDatabase.USER_TABLE)
  LiveData<List<User>> getAllUsers();

  // Deletes a specific user identified by their username.
  // This should be called from a background thread (Room does not allow DB writes on the main thread).
  @Query("DELETE FROM " + RockPaperScissorsDatabase.USER_TABLE +
      " WHERE username = :username")
  void deleteUserByUsername(String username);

  // Synchronously retrieves a single User object by username.
  // Returns null if no matching user exists.
  // Use this when you need an immediate value rather than LiveData (must be called off the main thread).
  @Query("SELECT * FROM " + RockPaperScissorsDatabase.USER_TABLE +
      " WHERE username = :username LIMIT 1")
  User getUserByUsernameSync(String username);
}