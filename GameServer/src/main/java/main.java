package main.java;

import org.eclipse.paho.client.mqttv3.*;

import java.util.List;

public class main {

    private static final String TAG = "IslandRush"; //unique identifier
    private static final String LOCALHOST = "127.0.0.1";
    private static final String MQTT_SERVER = "tcp://" + LOCALHOST + ":1883";

    private static LeaderboardOne lb1;
    private static LeaderboardTwo lb2;

    private final static String topicPrefixName = "/IslandRush/Leaderboard/PlayerName";
    private final static String topicPrefixTime = "/IslandRush/Leaderboard/Time";
    private final static String topicPrefixAvgSpeed = "/IslandRush/Leaderboard/AverageSpeed";

    private final static String playerName1 = topicPrefixName + "/1";
    private final static String playerName2 = topicPrefixName + "/2";
    private final static String playerName3 = topicPrefixName + "/3";
    private final static String playerName4 = topicPrefixName + "/4";
    private final static String playerName5 = topicPrefixName + "/5";

    private final static String time1 = topicPrefixTime + "/1";
    private final static String time2 = topicPrefixTime  + "/2";
    private final static String time3 = topicPrefixTime  + "/3";
    private final static String time4 = topicPrefixTime + "/4";
    private final static String time5 = topicPrefixTime  +  "/5";

    private final static String avgSpeed1 = topicPrefixAvgSpeed + "/1";
    private final static String avgSpeed2 = topicPrefixAvgSpeed  + "/2";
    private final static String avgSpeed3 = topicPrefixAvgSpeed  + "/3";
    private final static String avgSpeed4 = topicPrefixAvgSpeed  + "/4";
    private final static String avgSpeed5 = topicPrefixAvgSpeed + "/5";

    final static String firstPlayerName = topicPrefixName + "/2.1";
    final static String secondPlayerName = topicPrefixName + "/2.2";
    final static String thirdPlayerName = topicPrefixName + "/2.3";
    final static String fourthPlayerName = topicPrefixName + "/2.4";
    final static String fifthPlayerName = topicPrefixName + "/2.5";

    private static final String server = "/IslandRush/server";

    MQttClient client= new MQttClient(MQTT_SERVER,TAG);

    private static void createLeaderboard(){
        try {
            lb1 = Json.load("leaderboard1.json", LeaderboardOne.class);
            lb2 = Json.load("leaderboard2.json" , LeaderboardTwo.class);
        } catch (Exception e) {
            lb1 = new LeaderboardOne();
            lb2 = new LeaderboardTwo();
        }
    }

    private static void serializeLeaderboards() {
        try {
            Json.dump(lb1, "leaderboard.json");
            Json.dump(lb2, "leaderboard.json");
        } catch (Exception ignored) {}
    }

// test using dummy data to check whether the connection is working
    public static void main(String[] args) throws MqttException {
        MQttClient client= new MQttClient(MQTT_SERVER, TAG);
        client.connect(null);

        client.publish(playerName1,"Khaled");
        client.publish(playerName2,"HELLO");
        client.publish(playerName3,"Danesh");
        client.publish(playerName4, "NICOLE");
        client.publish(playerName5,"Bao");
        client.publish(time1,"10:30");
        client.publish(time2,"34:00");
        client.publish(time3,"54:00");
        client.publish(time4,"10:00");
        client.publish(time5,"60:00");
        client.publish(avgSpeed1,"70");
        client.publish(avgSpeed2,"38");
        client.publish(avgSpeed3,"90");
        client.publish(avgSpeed4,"50");
        client.publish(avgSpeed5,"60");
        client.publish(firstPlayerName,"Henry");
        client.publish(secondPlayerName,"John");
        client.publish(thirdPlayerName,"dimitrios");
        client.publish(fourthPlayerName,"dimitris");
        client.publish(fifthPlayerName,"kelly");
        while(client.isConnected){
            client.subscribe("/IslandRush/Server/name", new IMqttMessageListener() {
                public void messageArrived (String topic,MqttMessage message) throws Exception {
                    if (topic.equals("/IslandRush/Server/name")) {
                        String payload = new String(message.getPayload());
                        Time player = new Time(payload,1000);
                        System.out.println(payload);
                        System.out.println(player.toString());
                    }
                }
            });
        }
    }
}












/**to be implemented:
 * create leaderboard object and serialize to a json file
 * if a json file already exists then it is deserialized to a leaderboard object
 * MQTT connection will be used to get time and average speed values,then create
 * their correponding objects and add it to the leaderboard*/