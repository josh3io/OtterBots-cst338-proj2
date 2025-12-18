package edu.csumb.cst338.otterbots.rockpaperscissors.database.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Description: Entity for the RPS Round database table Author: Josh Goldberg Since: 2025.12.06
 */

@Entity(tableName = RockPaperScissorsDatabase.RPS_ROUND_TABLE)
public class RpsRound {

  @PrimaryKey(autoGenerate = true)
  private Integer id;

  @ColumnInfo(index = true)
  private int userStatsId;
  private int userChoice;
  private int npcChoice;
  private String result;
  private LocalDateTime date;

  public RpsRound(int userStatsId, int userChoice, int npcChoice, String result) {
    this.userStatsId = userStatsId;
    this.userChoice = userChoice;
    this.npcChoice = npcChoice;
    this.result = result;
    this.date = LocalDateTime.now();
  }

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public int getUserStatsId() {
    return userStatsId;
  }

  public void setUserStatsId(int userStatsId) {
    this.userStatsId = userStatsId;
  }

  public int getUserChoice() {
    return userChoice;
  }

  public void setUserChoice(int userChoice) {
    this.userChoice = userChoice;
  }

  public int getNpcChoice() {
    return npcChoice;
  }

  public void setNpcChoice(int npcChoice) {
    this.npcChoice = npcChoice;
  }

  public String getResult() {
    return result;
  }

  public void setResult(String result) {
    this.result = result;
  }

  public LocalDateTime getDate() {
    return date;
  }

  public void setDate(LocalDateTime date) {
    this.date = date;
  }

  @Override
  public boolean equals(Object o) {
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    RpsRound rpsRound = (RpsRound) o;
    return userStatsId == rpsRound.userStatsId && userChoice == rpsRound.userChoice
        && npcChoice == rpsRound.npcChoice && Objects.equals(id, rpsRound.id) && Objects.equals(
        result, rpsRound.result) && Objects.equals(date, rpsRound.date);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, userStatsId, userChoice, npcChoice, result, date);
  }

  @Override
  public String toString() {
    return "RpsRound{" +
        "id=" + id +
        ", userStatsId=" + userStatsId +
        ", userChoice=" + userChoice +
        ", npcChoice=" + npcChoice +
        ", result='" + result + '\'' +
        ", date=" + date +
        '}';
  }
}
