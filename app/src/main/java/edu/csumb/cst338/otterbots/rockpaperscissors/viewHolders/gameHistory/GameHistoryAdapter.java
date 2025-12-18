package edu.csumb.cst338.otterbots.rockpaperscissors.viewHolders.gameHistory;

import android.util.Log;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import edu.csumb.cst338.otterbots.rockpaperscissors.MainActivity;
import edu.csumb.cst338.otterbots.rockpaperscissors.database.entities.RpsRound;

public class GameHistoryAdapter extends ListAdapter<RpsRound, RecyclerView.ViewHolder> {

    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;

    public GameHistoryAdapter(@NonNull DiffUtil.ItemCallback<RpsRound> diffCallback) {
        super(diffCallback);
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == TYPE_HEADER) {
            return GameHistoryViewHeader.create(parent);
        } else if (viewType == TYPE_ITEM) {
            return GameHistoryViewHolder.create(parent);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof GameHistoryViewHolder) {
            GameHistoryViewHolder holderInstance = (GameHistoryViewHolder) holder;
            RpsRound current = getItem(position-1);
            Log.d(MainActivity.TAG, current.toString());
            holderInstance.bind(current);
        } else if (holder instanceof GameHistoryViewHeader) {
            GameHistoryViewHeader headerInstance = (GameHistoryViewHeader) holder;
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



    public static class GameHistoryDiff extends DiffUtil.ItemCallback<RpsRound> {
        @Override
        public boolean areItemsTheSame(@NonNull RpsRound oldItem, @NonNull RpsRound newItem) {
            return oldItem == newItem;
        }

        @Override
        public boolean areContentsTheSame(@NonNull RpsRound oldItem, @NonNull RpsRound newItem) {
            return oldItem.equals(newItem);
        }
    }
}
