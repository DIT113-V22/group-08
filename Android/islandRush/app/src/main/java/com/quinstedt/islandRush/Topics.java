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
}
