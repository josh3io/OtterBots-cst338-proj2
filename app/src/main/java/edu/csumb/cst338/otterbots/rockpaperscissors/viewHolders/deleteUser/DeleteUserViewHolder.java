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

    private final TextView userIdTextView;
    private final TextView usernameTextView;
    private final TextView isAdminTextView;

    public DeleteUserViewHolder(@NonNull View itemView) {   // ← must be public for adapter
        super(itemView);

        userIdTextView = itemView.findViewById(R.id.recyclerItemUserIdTextView);
        usernameTextView = itemView.findViewById(R.id.recyclerItemDeleteUsernameTextView);
        isAdminTextView = itemView.findViewById(R.id.recyclerItemUserIsAdmin);
    }

    public void bind(User user) {
        userIdTextView.setText(String.valueOf(user.getUserId()));
        usernameTextView.setText(user.getUsername());
        isAdminTextView.setText(user.getIsAdmin() == 1 ? "Yes" : "No");
    }

    public static DeleteUserViewHolder create(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.delete_user_recycler_item, parent, false);
        return new DeleteUserViewHolder(view);
    }
}
