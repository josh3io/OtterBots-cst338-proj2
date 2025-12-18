package edu.csumb.cst338.otterbots.rockpaperscissors.viewHolders.leaderboard;

import android.util.Log;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import edu.csumb.cst338.otterbots.rockpaperscissors.MainActivity;

/**
 * Description: display adapter for the leaderboard recycler view
 * Author: Josh Goldberg
 * Since: 2025.12.08
 */
public class LeaderboardAdapter extends ListAdapter<RankedUserStats, RecyclerView.ViewHolder> {
    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;

    public LeaderboardAdapter(@NonNull DiffUtil.ItemCallback<RankedUserStats> diffCallback) {
        super(diffCallback);
    }


    /**
     * Create the view holder
     *
     * @param parent   The ViewGroup into which the new View will be added after it is bound to
     *                 an adapter position.
     * @param viewType The view type of the new View.
     * @return
     */
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == TYPE_HEADER) {
            // we want to show a header as the first item of the view
            return LeaderboardViewHeader.create(parent);
        } else if (viewType == TYPE_ITEM) {
            return LeaderboardViewHolder.create(parent);
        }
        return null;
    }

    /**
     * Bind an object to the holder
     *
     * @param holder   the view holder to bind
     * @param position what position in the recycler to bind this to
     */
    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof LeaderboardViewHolder) {
            LeaderboardViewHolder holderInstance = (LeaderboardViewHolder) holder;
            RankedUserStats current = getItem(position - 1);
            Log.d(MainActivity.TAG, current.toString());
            holderInstance.bind(current);
        } else if (holder instanceof LeaderboardViewHeader) {
            LeaderboardViewHeader headerInstance = (LeaderboardViewHeader) holder;
            // use default text values for the header. no binding.
        }
    }

    /**
     * If it's the zeroth position, get a header. otherwise get an item
     *
     * @param position position to query
     * @return the type based on the position
     */
    @Override
    public int getItemViewType(int position) {
        return position == 0 ? TYPE_HEADER : TYPE_ITEM;
    }

    /**
     * Get a count of the items in the adapter
     *
     * @return total count of items, plus 1 for the header
     */
    @Override
    public int getItemCount() {
        return getCurrentList().size() + 1;
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
