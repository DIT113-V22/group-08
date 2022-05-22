package com.quinstedt.islandRush;


public class GlobalData {
     private static GlobalData globalData;
     public String playerData;
     public String time;
     public int timeInSec;
     public boolean isFinish;


     private GlobalData(){
         playerData = "";
         isFinish = false;
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

    public int getTimeInSec(Long time){
       long inSec = time /1000;
       timeInSec = (int) inSec;
         return timeInSec = (int) inSec;
    }

}
