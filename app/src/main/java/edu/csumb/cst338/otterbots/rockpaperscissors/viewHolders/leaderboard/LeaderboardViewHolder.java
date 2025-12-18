package edu.csumb.cst338.otterbots.rockpaperscissors.viewHolders.leaderboard;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import edu.csumb.cst338.otterbots.rockpaperscissors.MainActivity;
import edu.csumb.cst338.otterbots.rockpaperscissors.R;

/**
 * Description: Item holder for the leaderboard.
 * Author: Josh Goldberg
 * Since: 2025.12.08
 */
public class LeaderboardViewHolder extends RecyclerView.ViewHolder {
    private final TextView rankTextView;
    private final TextView usernameTextView;
    private final TextView winsTextView;
    private final TextView lossesTextView;
    private final TextView tiesTextView;

    private LeaderboardViewHolder(View leaderboardView) {
        super(leaderboardView);
        rankTextView = leaderboardView.findViewById(R.id.recyclerItemRankTextView);
        usernameTextView = leaderboardView.findViewById(R.id.recyclerItemUsernameTextView);
        winsTextView = leaderboardView.findViewById(R.id.recyclerItemWinsTextView);
        lossesTextView = leaderboardView.findViewById(R.id.recyclerItemLossesTextView);
        tiesTextView = leaderboardView.findViewById(R.id.recyclerItemTiesTextView);
    }

    /**
     * Bind the fields to the text views of the view
     * @param stats a RankedUserStats object that has all the data needed for display
     */
    public void bind(RankedUserStats stats) {
        Log.d(MainActivity.TAG,"User Stats binding "+stats);
        rankTextView.setText(String.valueOf(stats.getRank()));
        usernameTextView.setText(stats.getUsername());
        winsTextView.setText(String.valueOf(stats.getWins()));
        lossesTextView.setText(String.valueOf(stats.getLosses()));
        tiesTextView.setText(String.valueOf(stats.getTies()));
    }

    static LeaderboardViewHolder create(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.leaderboard_recycler_item, parent, false);
        return new LeaderboardViewHolder(view);
    }
}
