package edu.csumb.cst338.otterbots.rockpaperscissors.viewHolders.gameHistory;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.HashMap;

import edu.csumb.cst338.otterbots.rockpaperscissors.GamePlayActivity;
import edu.csumb.cst338.otterbots.rockpaperscissors.database.entities.RpsRound;

public class GameHistoryViewHolder extends RecyclerView.ViewHolder {
    private final TextView userChoiceTextView;
    private final TextView npcChoiceTextView;
    private final TextView resultTextView;
    private final TextView dateTextView;

    private HashMap<Integer,String> GAME_CHOICES;

    private GameHistoryViewHolder(View gameHistoryView) {
        super(gameHistoryView);
        GAME_CHOICES = GamePlayActivity.makeGameChoices();
        userChoiceTextView = gameHistoryView.findViewById(
    }

    public void bind(RpsRound round) {
        userChoiceTextView.setText(GAME_CHOICES.get(round.getUserChoice()));
        npcChoiceTextView.setText(GAME_CHOICES.get(round.getNpcChoice()));
        resultTextView.setText(round.getResult());
        dateTextView.setText(round.getDate());
    }
}
