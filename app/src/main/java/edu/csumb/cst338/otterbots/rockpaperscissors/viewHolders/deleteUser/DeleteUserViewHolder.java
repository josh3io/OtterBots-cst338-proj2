/**
 * DeleteUserViewHolder
 * ---------------------
 * ViewHolder responsible for binding user data to the delete-user
 * RecyclerView item layout.
 *
 * Displays: User ID, Username, and Admin status.
 *
 * Author: Christopher Buenrostro
 */
package edu.csumb.cst338.otterbots.rockpaperscissors.viewHolders.deleteUser;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import edu.csumb.cst338.otterbots.rockpaperscissors.R;
import edu.csumb.cst338.otterbots.rockpaperscissors.database.entities.User;

public class DeleteUserViewHolder extends RecyclerView.ViewHolder {

    // UI references from the item layout
    private final TextView userIdTextView;
    private final TextView usernameTextView;
    private final TextView isAdminTextView;

    /**
     * Constructor: initializes view references for this ViewHolder.
     * Must remain public because the adapter accesses it during inflation.
     */
    public DeleteUserViewHolder(@NonNull View itemView) {
        super(itemView);

        userIdTextView = itemView.findViewById(R.id.recyclerItemUserIdTextView);
        usernameTextView = itemView.findViewById(R.id.recyclerItemDeleteUsernameTextView);
        isAdminTextView = itemView.findViewById(R.id.recyclerItemUserIsAdmin);
    }

    /**
     * Binds a User object's properties to the corresponding UI elements.
     * Converts admin flag (1/0) into human-readable text.
     */
    public void bind(User user) {
        userIdTextView.setText(String.valueOf(user.getUserId()));
        usernameTextView.setText(user.getUsername());
        isAdminTextView.setText(user.getIsAdmin() == 1 ? "Yes" : "No");
    }

    /**
     * Helper method used by the adapter to inflate the item layout
     * and create a new ViewHolder instance.
     */
    public static DeleteUserViewHolder create(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.delete_user_recycler_item, parent, false);
        return new DeleteUserViewHolder(view);
    }
}