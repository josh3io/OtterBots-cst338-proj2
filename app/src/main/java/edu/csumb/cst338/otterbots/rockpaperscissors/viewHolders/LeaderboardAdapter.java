package edu.csumb.cst338.otterbots.rockpaperscissors.viewHolders;

import android.util.Log;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import edu.csumb.cst338.otterbots.rockpaperscissors.MainActivity;
import edu.csumb.cst338.otterbots.rockpaperscissors.RankedUserStats;
import edu.csumb.cst338.otterbots.rockpaperscissors.database.entities.UserJoinUserStats;
import edu.csumb.cst338.otterbots.rockpaperscissors.database.entities.UserStats;

public class LeaderboardAdapter extends ListAdapter<UserJoinUserStats, RecyclerView.ViewHolder> {
    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;

    public LeaderboardAdapter(@NonNull DiffUtil.ItemCallback<UserJoinUserStats> diffCallback) {
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
        //TODO: replace userId with userName
        RankedUserStats current = RankedUserStats.getRankedUserStats(position, "", getItem(position));
        Log.d(MainActivity.TAG, current.toString());
        if (holder instanceof LeaderboardViewHolder) {
            LeaderboardViewHolder holderInstance = (LeaderboardViewHolder) holder;
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

    public static class LeaderboardDiff extends DiffUtil.ItemCallback<UserJoinUserStats> {
        @Override
        public boolean areItemsTheSame(@NonNull UserJoinUserStats oldItem, @NonNull UserJoinUserStats newItem) {
            return oldItem == newItem;
        }

        @Override
        public boolean areContentsTheSame(@NonNull UserJoinUserStats oldItem, @NonNull UserJoinUserStats newItem) {
            return oldItem.equals(newItem);
        }
    }
}
