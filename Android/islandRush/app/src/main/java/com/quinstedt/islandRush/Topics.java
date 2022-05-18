package com.quinstedt.islandRush;

public class Topics {

    final static  String MAIN_TOPIC = "/IslandRush";
    public static final String CAMERA = MAIN_TOPIC + "/camera";

    public class Connection{
        public static final String TAG = "IslandRushApp";
        public static final String EXTERNAL_MQTT_BROKER = "aerostun.dev";
        public static final String LOCALHOST = "10.0.2.2";
        public static final String MQTT_SERVER = "tcp://" + LOCALHOST + ":1883";
        public static final int QOS = 1;
    }
    public class Sensor{
        static final String ODOMETER_DISTANCE = MAIN_TOPIC + "/Odometer/Distance";
        static final String ODOMETER_SPEED = MAIN_TOPIC + "/Odometer/Speed";
    }
    public class Controller {
        static final String CONTROLLER = MAIN_TOPIC + "/Control/Direction";
        static final String SET_CAR_SPEED = MAIN_TOPIC + "/Control/Speed";

    }
    public static class Server{

        public final static String SERVER = "/IslandRush/Server";
        public final static String Leaderboard1 = SERVER + "/Leaderboard1/PlayerName";
        public final static String Leaderboard2 = SERVER +"/Leaderboard2/PlayerName";
        public final static String Time = SERVER +"/Leaderboard1/Time";
        public final static String AvgSpeed = SERVER + "/Leaderboard2/AverageSpeed";

        public final static String playerName1 = Leaderboard1 + "/1";
        public final static String playerName2 = Leaderboard1 + "/2";
        public final static String playerName3 = Leaderboard1 + "/3";
        public final static String playerName4 = Leaderboard1 + "/4";
        public final static String playerName5 = Leaderboard1 + "/5";

        public final static String L2playerName1 = Leaderboard2 + "/1";
        public final static String L2playerName2 = Leaderboard2 + "/2";
        public final static String L2playerName3 = Leaderboard2 + "/3";
        public final static String L2playerName4 = Leaderboard2 + "/4";
        public final static String L2playerName5 = Leaderboard2 + "/5";

        public final static String raceTime1 = Time + "/1";
        public final static String raceTime2 = Time  + "/2";
        public final static String raceTime3 = Time  + "/3";
        public final static String raceTime4 = Time + "/4";
        public final static String raceTime5 = Time  +  "/5";

        public final static String averageSpeed1 = AvgSpeed + "/1";
        public final static String averageSpeed2 = AvgSpeed  + "/2";
        public final static String averageSpeed3 = AvgSpeed  + "/3";
        public final static String averageSpeed4 = AvgSpeed  + "/4";
        public final static String averageSpeed5 = AvgSpeed + "/5";


    }


}
