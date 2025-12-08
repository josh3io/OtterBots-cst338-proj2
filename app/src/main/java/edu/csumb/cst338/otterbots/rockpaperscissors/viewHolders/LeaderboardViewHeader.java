package edu.csumb.cst338.otterbots.rockpaperscissors.viewHolders;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import edu.csumb.cst338.otterbots.rockpaperscissors.R;
import edu.csumb.cst338.otterbots.rockpaperscissors.RankedUserStats;

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
