package edu.csumb.cst338.otterbots.rockpaperscissors.database.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Objects;

/**
 * Description: Entity for the user stats database table
 * Author: Josh Goldberg
 * Since: 2025.12.06
 */


@Entity(tableName = RockPaperScissorsDatabase.USER_STATS_TABLE)
public class UserStats {
    @PrimaryKey(autoGenerate = true)
    private Integer id;

    private Integer userId;
    private Integer wins;
    private Integer losses;
    private Integer ties;
    private Integer maxStreak;
    private Integer currentStreak;

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

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getWins() {
        return wins;
    }

    public void setWins(Integer wins) {
        this.wins = wins;
    }

    public Integer getLosses() {
        return losses;
    }

    public void setLosses(Integer losses) {
        this.losses = losses;
    }

    public Integer getTies() {
        return ties;
    }

    public void setTies(Integer ties) {
        this.ties = ties;
    }

    public Integer getMaxStreak() {
        return maxStreak;
    }

    public void setMaxStreak(Integer maxStreak) {
        this.maxStreak = maxStreak;
    }

    public Integer getCurrentStreak() {
        return currentStreak;
    }

    public void setCurrentStreak(Integer currentStreak) {
        this.currentStreak = currentStreak;
    }
}
