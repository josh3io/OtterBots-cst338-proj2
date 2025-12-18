package edu.csumb.cst338.otterbots.rockpaperscissors.viewHolders.deleteUser;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import edu.csumb.cst338.otterbots.rockpaperscissors.R;
import edu.csumb.cst338.otterbots.rockpaperscissors.database.entities.User;

public class DeleteUserViewAdapter extends ListAdapter<User, DeleteUserViewHolder> {

  private OnUserClickListener listener;

  public DeleteUserViewAdapter(@NonNull DiffUtil.ItemCallback<User> diffCallback) {
    super(diffCallback);
  }

  @NonNull
  @Override
  public DeleteUserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    View view = LayoutInflater.from(parent.getContext())
        .inflate(R.layout.delete_user_recycler_item, parent, false);
    return new DeleteUserViewHolder(view);
  }

  @Override
  public void onBindViewHolder(@NonNull DeleteUserViewHolder holder, int position) {
    User user = getItem(position);
    holder.bind(user);
  }

  public void setOnUserClickListener(OnUserClickListener listener) {
    this.listener = listener;
  }

  public interface OnUserClickListener {

    void onDeleteClick(User user);
  }

  public static class DeleteUserDiff extends DiffUtil.ItemCallback<User> {

    @Override
    public boolean areItemsTheSame(@NonNull User oldItem, @NonNull User newItem) {
      return oldItem.getUserId() == newItem.getUserId();
    }

    @Override
    public boolean areContentsTheSame(@NonNull User oldItem, @NonNull User newItem) {
      return oldItem.getUsername().equals(newItem.getUsername()) &&
          oldItem.getIsAdmin() == newItem.getIsAdmin();
    }
  }
}
