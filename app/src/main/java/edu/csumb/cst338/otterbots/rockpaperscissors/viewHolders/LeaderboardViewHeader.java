package edu.csumb.cst338.otterbots.rockpaperscissors.viewHolders;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import edu.csumb.cst338.otterbots.rockpaperscissors.R;
import edu.csumb.cst338.otterbots.rockpaperscissors.RankedUserStats;

public class LeaderboardViewHeader extends RecyclerView.ViewHolder {
    private final TextView rankTextView;
    private final TextView usernameTextView;
    private final TextView winsTextView;
    private final TextView lossesTextView;
    private final TextView tiesTextView;

    private LeaderboardViewHeader(View leaderboardView) {
        super(leaderboardView);
        rankTextView = leaderboardView.findViewById(R.id.recyclerItemRankTextView);
        usernameTextView = leaderboardView.findViewById(R.id.recyclerItemUsernameTextView);
        winsTextView = leaderboardView.findViewById(R.id.recyclerItemWinsTextView);
        lossesTextView = leaderboardView.findViewById(R.id.recyclerItemLossesTextView);
        tiesTextView = leaderboardView.findViewById(R.id.recyclerItemTiesTextView);
    }

    public void bind(RankedUserStats stats) {
        rankTextView.setText(stats.getRank());
        usernameTextView.setText(stats.getUserName());
        winsTextView.setText(stats.getWins());
        lossesTextView.setText(stats.getLosses());
        tiesTextView.setText(stats.getTies());
    }

    static LeaderboardViewHeader create(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.leaderboard_recycler_item, parent, false);
        return new LeaderboardViewHeader(view);
    }
}
