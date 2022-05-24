package com.quinstedt.islandRush.database;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "scoreboard_table")
public class PlayerScore {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String playerName;

    private int time;

    public PlayerScore(String playerName,int time) {
        this.playerName = playerName;
        this.time = time;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
