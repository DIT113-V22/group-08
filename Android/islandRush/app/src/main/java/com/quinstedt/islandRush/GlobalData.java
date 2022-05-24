package com.quinstedt.islandRush;

import androidx.appcompat.app.AppCompatActivity;

public class GlobalData extends AppCompatActivity {

    /**
     * This class is a singleton class
     * It is a static class so that data stored here can be accesses
     * by all the classes in the application
     */
     private static GlobalData globalData;
     public String playerData;
     public String time;
     public int timeInSec;



    private GlobalData(){
         playerData = "";
    }

     public  static GlobalData getGlobalData(){
            if( globalData == null){
                globalData = new GlobalData();
            }
            return globalData;
     }

    public String getPlayerData() {
         return playerData;
     }

    public String getRaceTime() {
        return time;
    }

    public void setPlayerData(String playerData) {
        this.playerData = playerData;
    }

    public void setRaceTime(String time) {
        this.time = time;
    }


    public void setTimeInSec(long timeInSec) {
        this.timeInSec = (int) timeInSec;
    }

    public int getTimeInSec(){
       long inSec = timeInSec /1000;
       timeInSec = (int) inSec;
         return timeInSec = (int) inSec;
    }



}
