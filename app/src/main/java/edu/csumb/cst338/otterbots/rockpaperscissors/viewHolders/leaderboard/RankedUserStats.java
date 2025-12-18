package edu.csumb.cst338.otterbots.rockpaperscissors.viewHolders.leaderboard;

import edu.csumb.cst338.otterbots.rockpaperscissors.database.entities.UserJoinUserStats;
import edu.csumb.cst338.otterbots.rockpaperscissors.database.entities.UserStats;
import java.util.Objects;

public class RankedUserStats extends UserStats {

  private int rank;
  private String username;

  public RankedUserStats(String username, UserStats userStats) {
    super(userStats);
    this.username = username;
  }

  public static RankedUserStats getRankedUserStats(int rank, UserJoinUserStats stats) {
    //RankedUserStats ranked = (RankedUserStats) stats;
    UserStats userStats = stats.getUserStats();
    if (userStats == null) {
      userStats = new UserStats(stats.getUserId(), 0, 0, 0, 0, 0);
    }
    RankedUserStats ranked = new RankedUserStats(
        stats.getUsername(),
        userStats
    );
    ranked.setRank(rank);

    return ranked;
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
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    if (!super.equals(o)) {
      return false;
    }
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

}
