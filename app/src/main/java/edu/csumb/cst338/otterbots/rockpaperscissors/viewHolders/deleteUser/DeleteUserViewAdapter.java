/**
 * DeleteUserViewAdapter
 * ----------------------
 * Adapter responsible for displaying user entries in a RecyclerView for the
 * purpose of selecting and deleting users. Utilizes ListAdapter + DiffUtil for
 * efficient UI updates.
 *
 * Author: Christopher Buenrostro
 */
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

  // Listener used to notify when a delete action is requested on a user item.
  private OnUserClickListener listener;

  /**
   * Constructor initializes the ListAdapter with a DiffUtil callback to optimize
   * RecyclerView updates when user lists change.
   */
  public DeleteUserViewAdapter(@NonNull DiffUtil.ItemCallback<User> diffCallback) {
    super(diffCallback);
  }

  /**
   * Inflates the RecyclerView item layout and returns a ViewHolder instance.
   */
  @NonNull
  @Override
  public DeleteUserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    View view = LayoutInflater.from(parent.getContext())
        .inflate(R.layout.delete_user_recycler_item, parent, false);
    return new DeleteUserViewHolder(view);
  }

  /**
   * Binds a User object to the ViewHolder based on the adapter position.
   */
  @Override
  public void onBindViewHolder(@NonNull DeleteUserViewHolder holder, int position) {
    User user = getItem(position);
    holder.bind(user);
  }

  /**
   * Interface callback allowing external components to handle delete
   * interactions for a particular user.
   */
  public interface OnUserClickListener {

    void onDeleteClick(User user);
  }

  /**
   * Assigns the listener responsible for handling delete actions.
   */
  public void setOnUserClickListener(OnUserClickListener listener) {
    this.listener = listener;
  }

  /**
   * DiffUtil implementation to efficiently determine changes in the User list.
   */
  public static class DeleteUserDiff extends DiffUtil.ItemCallback<User> {

    /**
     * Checks whether two items represent the same user in the database.
     */
    @Override
    public boolean areItemsTheSame(@NonNull User oldItem, @NonNull User newItem) {
      return oldItem.getUserId() == newItem.getUserId();
    }

    /**
     * Checks whether the content of two user objects is identical.
     */
    @Override
    public boolean areContentsTheSame(@NonNull User oldItem, @NonNull User newItem) {
      return oldItem.getUsername().equals(newItem.getUsername())
          && oldItem.getIsAdmin() == newItem.getIsAdmin();
    }
  }
}