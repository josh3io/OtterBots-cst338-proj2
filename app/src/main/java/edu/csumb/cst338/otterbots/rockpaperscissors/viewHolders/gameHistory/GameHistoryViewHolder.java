package edu.csumb.cst338.otterbots.rockpaperscissors.viewHolders.gameHistory;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.time.format.DateTimeFormatter;
import java.util.HashMap;

import edu.csumb.cst338.otterbots.rockpaperscissors.GamePlayActivity;
import edu.csumb.cst338.otterbots.rockpaperscissors.R;
import edu.csumb.cst338.otterbots.rockpaperscissors.database.entities.RpsRound;
import edu.csumb.cst338.otterbots.rockpaperscissors.viewHolders.leaderboard.LeaderboardViewHolder;

public class GameHistoryViewHolder extends RecyclerView.ViewHolder {
    private final TextView userChoiceTextView;
    private final TextView npcChoiceTextView;
    private final TextView resultTextView;
    private final TextView dateTextView;

    private HashMap<Integer,String> GAME_CHOICES;

    private GameHistoryViewHolder(View gameHistoryView) {
        super(gameHistoryView);
        GAME_CHOICES = GamePlayActivity.makeGameChoices();
        userChoiceTextView = gameHistoryView.findViewById(R.id.recyclerItemUserChoiceTextView);
        npcChoiceTextView = gameHistoryView.findViewById(R.id.recyclerItemNpcChoiceTextView);
        resultTextView = gameHistoryView.findViewById(R.id.recyclerItemResultTextView);
        dateTextView = gameHistoryView.findViewById(R.id.recyclerItemDateTextView);
    }

    public void bind(RpsRound round) {
        userChoiceTextView.setText(GAME_CHOICES.get(round.getUserChoice()));
        npcChoiceTextView.setText(GAME_CHOICES.get(round.getNpcChoice()));
        resultTextView.setText(round.getResult());

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EE h:mm a");

        dateTextView.setText(round.getDate().format(formatter));
    }
    static GameHistoryViewHolder create(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.game_history_recycler_item, parent, false);
        return new GameHistoryViewHolder(view);
    }
}
