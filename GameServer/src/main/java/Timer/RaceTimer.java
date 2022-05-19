/*package main.java.Timer;

/*import main.java.MQTTClient;


import java.time.Duration;
import java.time.Instant;

public class RaceTimer {

    private static Duration elapsedTime;
    private static Instant endTime ;
    private static Instant startTime;
    private static Instant pauseStart;
    private static Instant pauseEnd;
    private static Duration pauseTime= Duration.ZERO;





    public static  Duration getElapsedTime() {
        return elapsedTime;
    }

    public static void setElapsedTime(Duration eTime) {
        elapsedTime = eTime;
    }


    public static Instant getStartTime() {
        return startTime;
    }

    public static void setStartTime(Instant sTime) {
        startTime = sTime;
    }



    public static Instant getEndTime() {
        return endTime;
    }

    public static void setEndTime(Instant dTime) {
        endTime = dTime;
    }

    public static String textTime () {
        return elapsedTime.toString();
    }

    public static void startTimer () {
        setStartTime(Instant.now());
    }

    public static void endTimer () {
        setEndTime(Instant.now());
        calculateFinalTime();
    }

    public static void calculateFinalTime(){
        Duration d = Duration.between( getStartTime(), getEndTime());
        setElapsedTime(d);
    }
    public static double getCurrentTime () {
        return getElapsedTime().toSecondsPart();
    }

    public static void pauseTimer(){
        pauseStart= Instant.now();

    }

    public static void unPauseTimer () {
        pauseEnd=Instant.now();
        pauseTime=pauseTime.plus(Duration.between(pauseStart, pauseEnd));
    }

    public static String getFinalTime() {

        Duration elapsed = getElapsedTime().minus(pauseTime);
        int seconds = elapsed.toSecondsPart();
        int millis = elapsed.toMillisPart();
        int minutes = elapsed.toMinutesPart();
        String totalTime = Integer.toString(minutes) + ":" + Integer.toString(seconds) + ":"  + Integer.toString(millis);
        return totalTime;

    }

    public static double getAverageSpeed (double m){
        Duration elapsed = getElapsedTime().minus(pauseTime);
        double seconds= elapsed.toSeconds();
        return m/seconds;
    }

    public static void postResult (){
        MQTTClient m= new MQTTClient();
    m.connect();
    m.publish("elapsTime",getFinalTime());
    m.publish("avgSpeed",Double.toString(getAverageSpeed(30)));
    m.diConnect();
    }
}
**/