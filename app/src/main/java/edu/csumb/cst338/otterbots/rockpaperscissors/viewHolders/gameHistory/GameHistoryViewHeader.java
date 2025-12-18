package edu.csumb.cst338.otterbots.rockpaperscissors.viewHolders.gameHistory;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import edu.csumb.cst338.otterbots.rockpaperscissors.R;

public class GameHistoryViewHeader extends RecyclerView.ViewHolder {

    private GameHistoryViewHeader(View leaderboardView) {
        super(leaderboardView);
    }

    static GameHistoryViewHeader create(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.game_history_recycler_item, parent, false);
        return new GameHistoryViewHeader(view);
    }
}
