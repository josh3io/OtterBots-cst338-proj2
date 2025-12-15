package edu.csumb.cst338.otterbots.rockpaperscissors.viewHolders.deleteUser;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import android.app.Application;

import java.util.List;

import edu.csumb.cst338.otterbots.rockpaperscissors.database.entities.RockPaperScissorsRepository;
import edu.csumb.cst338.otterbots.rockpaperscissors.database.entities.User;


public class DeleteUserViewModel extends AndroidViewModel {
    private RockPaperScissorsRepository repository;

    public DeleteUserViewModel(Application application) {
        super(application);
        repository = RockPaperScissorsRepository.getRepository(application);
    }

    public LiveData<List<User>> getAllUsers() {
        return repository.getAllUsers();
    }

    public void deleteUserByUsername(String username) {
        repository.deleteUserByUsername(username);
    }
}
