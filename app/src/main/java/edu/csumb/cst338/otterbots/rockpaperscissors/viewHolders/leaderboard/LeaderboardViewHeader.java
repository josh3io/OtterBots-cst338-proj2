package edu.csumb.cst338.otterbots.rockpaperscissors.viewHolders.leaderboard;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import edu.csumb.cst338.otterbots.rockpaperscissors.R;

public class LeaderboardViewHeader extends RecyclerView.ViewHolder {

    private LeaderboardViewHeader(View leaderboardView) {
        super(leaderboardView);
    }

    static LeaderboardViewHeader create(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.leaderboard_recycler_item, parent, false);
        return new LeaderboardViewHeader(view);
    }
}
