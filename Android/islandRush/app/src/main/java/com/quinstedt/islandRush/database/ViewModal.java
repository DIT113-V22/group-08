package com.quinstedt.islandRush.database;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.room.Database;

import java.util.List;

public class ViewModal extends AndroidViewModel {
    // creating a new variable for score repository.
    private PlayerScoreRepository repository;

    // below line is to create a variable for live
    // data where all the player scores  are present.
    private LiveData<List<PlayerScore>> allPlayerScore;

    // constructor for view modal.
    public ViewModal(@NonNull Application application) {
        super(application);
        repository = new PlayerScoreRepository(application);
        allPlayerScore = repository.getAllScores();
    }

    // below method is use to insert the data to repository.
    public void insert(PlayerScore playerScore) {
        repository.insert(playerScore);
    }

    // below line is to update data in repository.
    public void update(PlayerScore playerScore) {
        repository.update(playerScore);
    }

    // below line is to delete the data in our repository.
    public void delete(PlayerScore playerScore) {
        repository.delete(playerScore);
    }

    // below method is to delete all the player scores in our list.
    public void deleteAllScores() {
        repository.deleteAllPlayerScores();
    }

    // below method is to get all the player scores in our list.
    public LiveData<List<PlayerScore>> getAllScores() {
        return allPlayerScore;
    }
}

