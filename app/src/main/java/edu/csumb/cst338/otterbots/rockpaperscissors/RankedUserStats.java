package edu.csumb.cst338.otterbots.rockpaperscissors;

import java.util.Objects;

import edu.csumb.cst338.otterbots.rockpaperscissors.database.entities.UserStats;

public class RankedUserStats extends UserStats {
    private int rank;
    private String userName;

    public RankedUserStats(int userId, int wins, int losses, int ties, int maxStreak, int currentStreak) {
        super(userId, wins, losses, ties, maxStreak, currentStreak);
    }


    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        RankedUserStats that = (RankedUserStats) o;
        return rank == that.rank && Objects.equals(userName, that.userName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), rank, userName);
    }

    @Override
    public String toString() {
        return "RankedUserStats{" +
                "rank=" + rank +
                ", userName='" + userName + '\'' +
                ", userStats=" + super.toString() +
                '}';
    }

    public static RankedUserStats getRankedUserStats(int rank, String userName, UserStats stats) {
        //RankedUserStats ranked = (RankedUserStats) stats;
        RankedUserStats ranked = new RankedUserStats(
                stats.getUserId(),
                stats.getWins(),
                stats.getLosses(),
                stats.getTies(),
                stats.getMaxStreak(),
                stats.getCurrentStreak());
        ranked.setRank(rank);
        ranked.setUserName(userName);

        return ranked;
    }

}
