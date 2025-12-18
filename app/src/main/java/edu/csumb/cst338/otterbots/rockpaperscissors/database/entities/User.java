package edu.csumb.cst338.otterbots.rockpaperscissors.database.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * Description: Author: Murtaza Badri Since: 12/09/2025
 */
@Entity(tableName = RockPaperScissorsDatabase.USER_TABLE)
public class User {

  @PrimaryKey(autoGenerate = true)
  private int userId;
  private String username;
  private String password;
  private int isAdmin;

  public User(String username, String password, int isAdmin) {
    this.username = username;
    this.password = password;
    this.isAdmin = isAdmin;
  }

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
