package edu.csumb.cst338.otterbots.rockpaperscissors.database.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * Description: Entity class representing a user in the Rock-Paper-Scissors application.
 * Author: Murtaza Badri
 * Since: 12/09/2025
 */

// Room Entity for User table
@Entity(tableName = RockPaperScissorsDatabase.USER_TABLE)
public class User {

  // Primary key with auto-generation
  @PrimaryKey(autoGenerate = true)
  private int userId;
  private String username;
  private String password;
  private int isAdmin;
  // Constructor
  public User(String username, String password, int isAdmin) {
    this.username = username;
    this.password = password;
    this.isAdmin = isAdmin;
  }
  // Getters and Setters
  public int getUserId() {
    return userId;
  }

  public void setUserId(int userId) {
    this.userId = userId;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public int getIsAdmin() {
    return isAdmin;
  }

  public void setIsAdmin(int isAdmin) {
    this.isAdmin = isAdmin;
  }
}
