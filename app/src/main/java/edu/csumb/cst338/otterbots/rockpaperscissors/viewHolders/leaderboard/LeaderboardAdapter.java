package edu.csumb.cst338.otterbots.rockpaperscissors.viewHolders.leaderboard;

import android.util.Log;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import edu.csumb.cst338.otterbots.rockpaperscissors.MainActivity;

public class LeaderboardAdapter extends ListAdapter<RankedUserStats, RecyclerView.ViewHolder> {
    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;

    public LeaderboardAdapter(@NonNull DiffUtil.ItemCallback<RankedUserStats> diffCallback) {
        super(diffCallback);
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == TYPE_HEADER) {
            return LeaderboardViewHeader.create(parent);
        } else if (viewType == TYPE_ITEM) {
            return LeaderboardViewHolder.create(parent);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof LeaderboardViewHolder) {
            LeaderboardViewHolder holderInstance = (LeaderboardViewHolder) holder;
            RankedUserStats current = getItem(position-1);
            Log.d(MainActivity.TAG, current.toString());
            holderInstance.bind(current);
        } else if (holder instanceof LeaderboardViewHeader) {
            LeaderboardViewHeader headerInstance = (LeaderboardViewHeader) holder;
            // use default text values for the header. no binding.
        }
    }

    @Override
    public int getItemViewType(int position) {
        return position == 0 ? TYPE_HEADER : TYPE_ITEM;
    }

    @Override
    public int getItemCount() {
        return getCurrentList().size()+1;
    }



    public static class LeaderboardDiff extends DiffUtil.ItemCallback<RankedUserStats> {
        @Override
        public boolean areItemsTheSame(@NonNull RankedUserStats oldItem, @NonNull RankedUserStats newItem) {
            return oldItem == newItem;
        }

        @Override
        public boolean areContentsTheSame(@NonNull RankedUserStats oldItem, @NonNull RankedUserStats newItem) {
            return oldItem.equals(newItem);
        }
    }
}
