package edu.csumb.cst338.otterbots.rockpaperscissors.viewHolders.leaderboard;

import java.util.Objects;

import edu.csumb.cst338.otterbots.rockpaperscissors.database.entities.UserJoinUserStats;
import edu.csumb.cst338.otterbots.rockpaperscissors.database.entities.UserStats;

public class RankedUserStats extends UserStats {
    private int rank;
    private String username;

    public RankedUserStats(int userId, String username, UserStats userStats) {
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

    public static RankedUserStats getRankedUserStats(int rank, String username, UserJoinUserStats stats) {
        //RankedUserStats ranked = (RankedUserStats) stats;
        RankedUserStats ranked = new RankedUserStats(
                stats.getUserId(),
                stats.getUsername(),
                stats.getUserStats());
        ranked.setRank(rank);

        return ranked;
    }

}
