package edu.csumb.cst338.otterbots.rockpaperscissors.database.entities;

import androidx.room.Embedded;
import androidx.room.Entity;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * Description: entity for the join query of userLogin and userStats
 * Author: Josh Goldberg
 * Since: 2025.12.14
 */
@Entity
public class UserJoinUserStats {
    private int userId;
    private String username;

    @Embedded(prefix = "us_")
    private UserStats userStats;

    @Override
    public String toString() {
        return "UserJoinUserStats{" +
                "userId=" + userId +
                ", username='" + username + '\'' +
                ", userStats=" + userStats +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        UserJoinUserStats that = (UserJoinUserStats) o;
        return userId == that.userId && Objects.equals(username, that.username) && Objects.equals(userStats, that.userStats);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, username, userStats);
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

    public UserStats getUserStats() {
        return userStats;
    }

    public void setUserStats(UserStats userStats) {
        this.userStats = userStats;
    }
}
