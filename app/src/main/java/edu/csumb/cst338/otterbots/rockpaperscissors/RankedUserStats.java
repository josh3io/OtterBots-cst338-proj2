package edu.csumb.cst338.otterbots.rockpaperscissors;

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

    public static RankedUserStats getRankedUserStats(int rank, String userName, UserStats stats) {
        RankedUserStats ranked = (RankedUserStats) stats;
        ranked.setRank(rank);
        ranked.setUserName(userName);

        return ranked;
    }

}
