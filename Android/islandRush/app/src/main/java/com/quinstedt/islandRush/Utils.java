package com.quinstedt.islandRush;

public class Utils {
    public static void delay(int time){
        try{
            Thread.sleep(time);
        }catch(Exception exception){
            exception.getStackTrace();
        }
    }
}
