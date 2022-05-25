package com.quinstedt.islandRush.database;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

public class PlayerScoreRepository {

    private ScoreDao dao;
    private LiveData<List<PlayerScore>> allPlayerScores;

    public PlayerScoreRepository(Application application) {
        PlayerScoreDatabase database = PlayerScoreDatabase.getInstance(application);
        dao = database.Dao();
        allPlayerScores = dao.getAllPlayerScores();
    }

    // creating a method to insert the data to our database.
    public void insert(PlayerScore playerScore) {
        new InsertScoreAsyncTask(dao).execute(playerScore);
    }

    // creating a method to update data in database.
    public void update(PlayerScore playerScore) {
        new UpdateScoreAsyncTask(dao).execute(playerScore);
    }

    // creating a method to delete the data in our database.
    public void delete(PlayerScore playerScore) {
        new DeleteScoreAsyncTask(dao).execute(playerScore);
    }

    // below is the method to delete all the player scores.
    public void deleteAllPlayerScores() {
        new DeleteAllScoresAsyncTask(dao).execute();
    }

    // below method is to read all the player scores.
    public LiveData<List<PlayerScore>> getAllScores() {
        return allPlayerScores;
    }

    // we are creating a async task method to insert new player score.
    private static class InsertScoreAsyncTask extends AsyncTask< PlayerScore, Void, Void> {
        private ScoreDao dao;

        private InsertScoreAsyncTask(ScoreDao dao) {
            this.dao = dao;
        }

        @Override
        protected Void doInBackground(PlayerScore... playerScore) {
            // below line is use to insert our player Score in dao.
            dao.insert(playerScore[0]);
            return null;
        }
    }

    // we are creating a async task method to update player scores.
    private static class UpdateScoreAsyncTask extends AsyncTask<PlayerScore, Void, Void> {
        private ScoreDao dao;

        private UpdateScoreAsyncTask(ScoreDao dao) {
            this.dao = dao;
        }

        @Override
        protected Void doInBackground(PlayerScore... playerScores) {
            // below line is use to update
            // our playerScores in dao.
            dao.update(playerScores[0]);
            return null;
        }
    }

    // we are creating a async task method to delete player scores.
    private static class DeleteScoreAsyncTask extends AsyncTask<PlayerScore, Void, Void> {
        private ScoreDao dao;

        private DeleteScoreAsyncTask(ScoreDao dao) {
            this.dao = dao;
        }

        @Override
        protected Void doInBackground(PlayerScore... playerScores) {
            // below line is use to delete
            // our player score in dao.
            dao.delete(playerScores[0]);
            return null;
        }
    }

    // we are creating a async task method to delete all player scores.
    private static class DeleteAllScoresAsyncTask extends AsyncTask<Void, Void, Void> {
        private ScoreDao dao;
        private DeleteAllScoresAsyncTask(ScoreDao dao) {
            this.dao = dao;
        }
        @Override
        protected Void doInBackground(Void... voids) {
            // on below line calling method
            // to delete all player scores.
            dao.deleteAllScores();
            return null;
        }
    }
}

