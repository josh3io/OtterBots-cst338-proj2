package edu.csumb.cst338.otterbots.rockpaperscissors.database.entities;

import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.util.Objects;

import edu.csumb.cst338.otterbots.rockpaperscissors.R;

/**
 * Description: Entity for the user stats database table
 * Author: Josh Goldberg
 * Since: 2025.12.06
 */


@Entity(
        tableName = RockPaperScissorsDatabase.USER_STATS_TABLE,
        indices = {@Index(value={"userId"}, unique = true)}
)
public class UserStats {
    @PrimaryKey(autoGenerate = true)
    private Integer id;

    private int userId;
    private int wins;
    private int losses;
    private int ties;
    private int maxStreak;
    private int currentStreak;

    public UserStats(UserStats oldUserStats) {
        this.id = oldUserStats.id;
        this.userId = oldUserStats.userId;
        this.wins = oldUserStats.wins;
        this.losses = oldUserStats.losses;
        this.ties = oldUserStats.ties;
        this.maxStreak = oldUserStats.maxStreak;
        this.currentStreak = oldUserStats.currentStreak;
    }
    public UserStats(int userId, int wins, int losses, int ties, int maxStreak, int currentStreak) {
        this.userId = userId;
        this.wins = wins;
        this.losses = losses;
        this.ties = ties;
        this.maxStreak = maxStreak;
        this.currentStreak = currentStreak;
    }

    @Override
    public String toString() {
        return "UserStats{" +
                "id=" + id +
                ", userId=" + userId +
                ", wins=" + wins +
                ", losses=" + losses +
                ", ties=" + ties +
                ", maxStreak=" + maxStreak +
                ", currentStreak=" + currentStreak +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        UserStats userStats = (UserStats) o;
        return Objects.equals(id, userStats.id) && Objects.equals(userId, userStats.userId) && Objects.equals(wins, userStats.wins) && Objects.equals(losses, userStats.losses) && Objects.equals(ties, userStats.ties) && Objects.equals(maxStreak, userStats.maxStreak) && Objects.equals(currentStreak, userStats.currentStreak);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, userId, wins, losses, ties, maxStreak, currentStreak);
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getWins() {
        return wins;
    }

    public void setWins(int wins) {
        this.wins = wins;
    }

    public int getLosses() {
        return losses;
    }

    public void setLosses(int losses) {
        this.losses = losses;
    }

    public int getTies() {
        return ties;
    }

    public void setTies(int ties) {
        this.ties = ties;
    }

    public int getMaxStreak() {
        return maxStreak;
    }

    public void setMaxStreak(int maxStreak) {
        this.maxStreak = maxStreak;
    }

    public int getCurrentStreak() {
        return currentStreak;
    }

    public void setCurrentStreak(int currentStreak) {
        this.currentStreak = currentStreak;
    }
}

