package main.java;

import static java.lang.Double.compare;

public class Time implements Comparable<Time> {

    private String playerName;
    private double time;


    public Time(String playerName, double time) {
        this.playerName = playerName;
        this.time = time;
    }

    //Default Constructor
    public Time() {
        playerName = "";
        time = 0;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public double getTime() {
        return time;
    }

    public void setTime(double time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return String.format("Name: %s, Time Taken: %f", playerName, time);
    }

    @Override
    public int compareTo(Time otherTime) {
        return compare( getTime(),otherTime.getTime());
    }

}


