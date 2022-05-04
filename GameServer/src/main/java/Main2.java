package com.company;




public class Main {



    public static void main(String[] args) {

        RaceTimer.startTimer();
        Main main = new Main();
    try{
        Thread.sleep(1500);
    } catch ( Exception e){

    }
    RaceTimer.pauseTimer();
        try{
            Thread.sleep(1500);
        } catch ( Exception e){

        }
        RaceTimer.unPauseTimer();
        try{
            Thread.sleep(1500);
        } catch ( Exception e){

        }



        RaceTimer.endTimer();
        System.out.println(RaceTimer.getFinalTime());
        System.out.println("time");

        System.out.println(RaceTimer.getAverageSpeed(6));
        RaceTimer.postResult();
    }


}
