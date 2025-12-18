package edu.csumb.cst338.otterbots.rockpaperscissors.database.entities;
/**
 * Description: DAO interface for accessing User data in the Rock-Paper-Scissors application.
 * Author: Murtaza Badri
 * Since: 12/07/2025
 */

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import java.util.List;

// Room DAO for User entity
@Dao
public interface UserDAO {
    // Insert a new user into the database
  @Insert
  long insert(User user);

  //Retrieve a user by username (observable)
  @Query("SELECT * FROM " + RockPaperScissorsDatabase.USER_TABLE +
      " WHERE username = :username LIMIT 1")
  LiveData<User> getUserByUsername(String username);

  // Retrieve a user for login validation
  @Query("SELECT * FROM " + RockPaperScissorsDatabase.USER_TABLE +
      " WHERE username = :username AND password = :password LIMIT 1")
  LiveData<User> getUserLogin(String username, String password);

  //Retrieve a user by userId (observable)
  @Query("SELECT * FROM " + RockPaperScissorsDatabase.USER_TABLE +
      " WHERE userId = :id LIMIT 1")
  LiveData<User> getUserById(int id);

  //Update a user's username
  @Query("UPDATE " + RockPaperScissorsDatabase.USER_TABLE +
      " SET username = :newUsername WHERE userId = :userId")
  int updateUsername(int userId, String newUsername);

//Update a user's password
  @Query("UPDATE " + RockPaperScissorsDatabase.USER_TABLE +
      " SET password = :newPassword WHERE userId = :userId")
  int updatePassword(int userId, String newPassword);

  //TODO configure this with rps repo and database to be used in deleteuserviewmodel
  @Query("SELECT * FROM " + RockPaperScissorsDatabase.USER_TABLE)
  LiveData<List<User>> getAllUsers();

  // Delete a user by username
  @Query("DELETE FROM " + RockPaperScissorsDatabase.USER_TABLE +
      " WHERE username = :username")
  void deleteUserByUsername(String username);

  //Synchronous user lookup (used in testing or validation)
  @Query("SELECT * FROM " + RockPaperScissorsDatabase.USER_TABLE +
      " WHERE username = :username LIMIT 1")
  User getUserByUsernameSync(String username);
}
