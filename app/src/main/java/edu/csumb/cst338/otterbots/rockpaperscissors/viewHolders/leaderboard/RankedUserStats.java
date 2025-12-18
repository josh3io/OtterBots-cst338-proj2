package edu.csumb.cst338.otterbots.rockpaperscissors.viewHolders.leaderboard;

import java.util.Objects;

import edu.csumb.cst338.otterbots.rockpaperscissors.database.entities.UserJoinUserStats;
import edu.csumb.cst338.otterbots.rockpaperscissors.database.entities.UserStats;

/**
 * Description: A wrapper around the UserStats entity that adds rank and username fields for
 * displaying on the leaderboard activity
 * Author: Josh Goldberg
 * Since: 2025.12.14
 */
public class RankedUserStats extends UserStats {
    private int rank;
    private String username;

    public RankedUserStats(String username, UserStats userStats) {
        super(userStats);
        this.username = username;
    }


    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        RankedUserStats that = (RankedUserStats) o;
        return rank == that.rank && Objects.equals(username, that.username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), rank, username);
    }

    @Override
    public String toString() {
        return "RankedUserStats{" +
                "rank=" + rank +
                ", userName='" + username + '\'' +
                ", userStats=" + super.toString() +
                '}';
    }

    /**
     * Factory function for a new RankedUserStats object
     *
     * @param rank  the rank to show for this object on the leaderboard
     * @param stats the object that is a room entity join of user and user stats tables
     * @return a new RankedUserStats object
     */
    public static RankedUserStats getRankedUserStats(int rank, UserJoinUserStats stats) {
        UserStats userStats = stats.getUserStats();
        if (userStats == null) {
            // this could be null since it's from a left join
            // so if it is, default to zeroes for this user
            userStats = new UserStats(stats.getUserId(), 0, 0, 0, 0, 0);
        }
        RankedUserStats ranked = new RankedUserStats(
                stats.getUsername(),
                userStats
        );
        ranked.setRank(rank);

        return ranked;
    }

}
