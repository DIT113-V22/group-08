package com.quinstedt.islandRush.database;

import androidx.lifecycle.LiveData;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@androidx.room.Dao
public interface ScoreDao {

    @Insert
    void insert(PlayerScore playerScore);

    @Update
    void update(PlayerScore playerScore);

    @Delete
    void delete(PlayerScore playerScore);

    @Query("DELETE FROM scoreboard_table")
    void deleteAllScores();

    @Query("SELECT * FROM scoreboard_table ORDER BY time ASC LIMIT 5")
    LiveData<List<PlayerScore>> getAllPlayerScores();
}
