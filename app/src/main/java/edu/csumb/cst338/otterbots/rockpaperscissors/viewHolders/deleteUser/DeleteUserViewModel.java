/**
 * DeleteUserViewModel
 * --------------------
 * ViewModel responsible for providing user data to the UI layer and
 * delegating delete operations to the repository.
 *
 * This ViewModel exposes LiveData for observing the list of users and
 * provides a method for deleting a user by username.
 *
 * Author: Christopher Buenrostro
 */
package edu.csumb.cst338.otterbots.rockpaperscissors.viewHolders.deleteUser;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import edu.csumb.cst338.otterbots.rockpaperscissors.database.entities.RockPaperScissorsRepository;
import edu.csumb.cst338.otterbots.rockpaperscissors.database.entities.User;
import java.util.List;

public class DeleteUserViewModel extends AndroidViewModel {

    // Reference to the repository, which handles database operations.
    private final RockPaperScissorsRepository repository;

    /**
     * Constructor initializes the repository using the application context.
     * AndroidViewModel is used so the ViewModel can access the application
     * when needed (e.g., for creating Room database instances).
     */
    public DeleteUserViewModel(Application application) {
        super(application);
        repository = RockPaperScissorsRepository.getRepository(application);
    }

    /**
     * Returns a LiveData list of all users stored in the database.
     * The UI observes this list so it can automatically update when data changes.
     */
    public LiveData<List<User>> getAllUsers() {
        return repository.getAllUsers();
    }

    /**
     * Requests deletion of a user by their username.
     * Actual database write is handled inside the repository (background thread).
     */
    public void deleteUserByUsername(String username) {
        repository.deleteUserByUsername(username);
    }
}