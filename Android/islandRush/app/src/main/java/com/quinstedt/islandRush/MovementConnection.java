package com.quinstedt.islandRush;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.util.Arrays;

public class MovementConnection extends AppCompatActivity {
    // CONNECTION TO MQTT BROKER
     static final String TAG = "IslandRush";
     static final String EXTERNAL_MQTT_BROKER = "aerostun.dev";
     static final String LOCALHOST = "10.0.2.2";
     static final String MQTT_SERVER = "tcp://" + LOCALHOST + ":1883";
     static final int QOS = 1;
     boolean isConnected = false;
     MqttClient mMqttClient;

    // MQTT TOPICS
    static final String MAIN_TOPIC = "/IslandRush";

    static final String CAMERA = MAIN_TOPIC + "/camera";
    //Sensor topics
   // static final String ULTRASOUND = MAIN_TOPIC +  "/ultrasound/front";
    static final String ODOMETER_DISTANCE = MAIN_TOPIC + "/Odometer/LeftDistance";
    static final String ODOMETER_SPEED = MAIN_TOPIC + "/Odometer/RightSpeed";
    //Controller topics
    static final String CONTROLLER = MAIN_TOPIC + "/Control/Direction";
    static final String SPEED = MAIN_TOPIC + "/Control/Speed";

    // CAMERA
     static final int IMAGE_WIDTH = 320;
     static final int IMAGE_HEIGHT = 240;
     ImageView mCameraView;

    // SERVER
    final static String topicPrefixName = MAIN_TOPIC + "/Leaderboard/PlayerName";
    final static String topicPrefixTime = MAIN_TOPIC + "/Leaderboard/Time";
    final static String topicPrefixAvgSpeed = MAIN_TOPIC + "/Leaderboard/AverageSpeed";

    final static String playerName1 = topicPrefixName + "/1";
    final static String playerName2 = topicPrefixName + "/2";
    final static String playerName3 = topicPrefixName + "/3";
    final static String playerName4 = topicPrefixName + "/4";
    final static String playerName5 = topicPrefixName + "/5";

    final static String firstPlayerName = topicPrefixName + "/2.1";
    final static String secondPlayerName = topicPrefixName + "/2.2";
    final static String thirdPlayerName = topicPrefixName + "/2.3";
    final static String fourthPlayerName = topicPrefixName + "/2.4";
    final static String fifthPlayerName = topicPrefixName + "/2.5";

    final static String time1 = topicPrefixTime + "/1";
    final static String time2 = topicPrefixTime  + "/2";
    final static String time3 = topicPrefixTime  + "/3";
    final static String time4 = topicPrefixTime + "/4";
    final static String time5 = topicPrefixTime  +  "/5";

    final static String avgSpeed1 = topicPrefixAvgSpeed + "/1";
    final static String avgSpeed2 = topicPrefixAvgSpeed  + "/2";
    final static String avgSpeed3 = topicPrefixAvgSpeed + "/3";
    final static String avgSpeed4 = topicPrefixAvgSpeed  + "/4";
    final static String avgSpeed5 = topicPrefixAvgSpeed + "/5";

    TextView player1;
    TextView player2;
    TextView player3;
    TextView player4;
    TextView player5;

    TextView firstTime;
    TextView secondTime;
    TextView thirdTime;
    TextView fourthTime;
    TextView fifthTime;

    TextView speed1;
    TextView speed2;
    TextView speed3;
    TextView speed4;
    TextView speed5;

    TextView firstPlayer;
    TextView secondPlayer;
    TextView thirdPlayer;
    TextView fourPlayer;
    TextView fifthPlayer;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mMqttClient = new MqttClient(getApplicationContext(), MQTT_SERVER, TAG);
        mCameraView = findViewById(R.id.controlPad_camera);
        player1 = findViewById(R.id.player1);
        player2 = findViewById(R.id.player2);
        player3 = findViewById(R.id.player3);
        player4 = findViewById(R.id.player4);
        player5 = findViewById(R.id.player5);
        firstPlayer = findViewById(R.id.firstPlayer);
        secondPlayer = findViewById(R.id.secondPlayer);
        thirdPlayer = findViewById(R.id.thirdPlayer);
        fourPlayer = findViewById(R.id.fourthPlayer);
        fifthPlayer = findViewById(R.id.fifthPlayer);
        firstTime = findViewById(R.id.time1);
        secondTime = findViewById(R.id.time2);
        thirdTime = findViewById(R.id.time3);
        fourthTime = findViewById(R.id.time4);
        fifthTime = findViewById(R.id.time5);
        speed1 = findViewById(R.id.avgSpeed1);
        speed2 = findViewById(R.id.avgSpeed2);
        speed3 = findViewById(R.id.avgSpeed3);
        speed4 = findViewById(R.id.avgSpeed4);
        speed5 = findViewById(R.id.avgSpeed5);

        connectToMqttBroker();
    }


    @Override
    protected void onResume() {
        super.onResume();

        connectToMqttBroker();
    }

    @Override
    protected void onPause() {
        super.onPause();

        mMqttClient.disconnect(new IMqttActionListener() {
            @Override
            public void onSuccess(IMqttToken asyncActionToken) {
                Log.i(TAG, "Disconnected from broker");
            }

            @Override
            public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
            }
        });
    }

    public void connectToMqttBroker() {
        if (!isConnected) {
            mMqttClient.connect(TAG, "", new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    isConnected = true;

                    final String successfulConnection = "Connected to MQTT broker";
                    Log.i(TAG, successfulConnection);
                    Toast.makeText(getApplicationContext(), successfulConnection, Toast.LENGTH_SHORT).show();
                  //  mMqttClient.subscribe(ULTRASOUND, QOS, null);
                    mMqttClient.subscribe(ODOMETER_SPEED, QOS, null);
                    mMqttClient.subscribe(ODOMETER_DISTANCE, QOS, null);
                    mMqttClient.subscribe(CAMERA, QOS, null);
                    mMqttClient.subscribe(topicPrefixName + "/#", QOS, null);
                    mMqttClient.subscribe(topicPrefixTime + "/#", QOS, null);
                    mMqttClient.subscribe(topicPrefixAvgSpeed + "/#", QOS, null);
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    final String failedConnection = "Failed to connect to MQTT broker";
                    Log.e(TAG, failedConnection);
                    Toast.makeText(getApplicationContext(), failedConnection, Toast.LENGTH_SHORT).show();
                }
            }, new MqttCallback() {
                @Override
                public void connectionLost(Throwable cause) {
                    isConnected = false;

                    final String connectionLost = "Connection to MQTT broker lost";
                    Log.w(TAG, connectionLost);
                    Toast.makeText(getApplicationContext(), connectionLost, Toast.LENGTH_SHORT).show();
                }


                @Override
                public void messageArrived(String topic, MqttMessage message) throws Exception {
                    if (topic.equals(CAMERA)) {
                        final Bitmap bm = Bitmap.createBitmap(IMAGE_WIDTH, IMAGE_HEIGHT, Bitmap.Config.ARGB_8888);

                        final byte[] payload = message.getPayload();
                        final int[] colors = new int[IMAGE_WIDTH * IMAGE_HEIGHT];
                        for (int ci = 0; ci < colors.length; ++ci) {
                            final byte r = payload[3 * ci];
                            final byte g = payload[3 * ci + 1];
                            final byte b = payload[3 * ci + 2];
                            colors[ci] = Color.rgb(r, g, b);
                        }
                        bm.setPixels(colors, 0, IMAGE_WIDTH, 0, 0, IMAGE_WIDTH, IMAGE_HEIGHT);
                        mCameraView.setImageBitmap(bm);
                    } else {
                        Log.i(TAG, "[MQTT] Topic: " + topic + " | Message: " + message.toString());
                    }
                    if (topic.equals(playerName1)) {
                        String payload = new String(message.getPayload());
                        player1.setText(payload);
                    }
                    if (topic.equals(playerName2)) {
                        String payload = new String(message.getPayload());
                        player2.setText(payload);
                    }
                    if (topic.equals(playerName3)) {
                        String payload = new String(message.getPayload());
                        player3.setText(payload);
                    }
                    if (topic.equals(playerName4)) {
                        String payload = new String(message.getPayload());
                        player4.setText(payload);
                    }
                    if (topic.equals(playerName5)) {
                        String payload = new String(message.getPayload());
                        player5.setText(payload);
                    }
                    if (topic.equals(firstPlayerName)) {
                        String payload = new String(message.getPayload());
                        firstPlayer.setText(payload);
                    }
                    if (topic.equals(secondPlayerName)) {
                        String payload = new String(message.getPayload());
                        secondPlayer.setText(payload);
                    }
                    if (topic.equals(thirdPlayerName)) {
                        String payload = new String(message.getPayload());
                        thirdPlayer.setText(payload);
                    }
                    if (topic.equals(fourthPlayerName)) {
                        String payload = new String(message.getPayload());
                        fourPlayer.setText(payload);
                    }
                    if (topic.equals(fifthPlayerName)) {
                        String payload = new String(message.getPayload());
                        fifthPlayer.setText(payload);
                    }
                    if (topic.equals(time1)) {
                        String text = new String(message.getPayload());
                        firstTime.setText(text);
                    }
                    if (topic.equals(time2)) {
                        String text = new String(message.getPayload());
                        secondTime.setText(text);
                    }
                    if (topic.equals(time3)) {
                        String text = new String(message.getPayload());
                        thirdTime.setText(text);
                    }
                    if (topic.equals(time4)) {
                        String text = new String(message.getPayload());
                        fourthTime.setText(text);
                    }
                    if (topic.equals(time5)) {
                        String text = new String(message.getPayload());
                        fifthTime.setText(text);
                    }
                    if (topic.equals(avgSpeed1)) {
                        String text = new String(message.getPayload());
                        speed1.setText(text);
                    }
                    if (topic.equals(avgSpeed2)) {
                        String text = new String(message.getPayload());
                        speed2.setText(text);
                    }
                    if (topic.equals(avgSpeed3)) {
                        String text = new String(message.getPayload());
                        speed3.setText(text);
                    }
                    if (topic.equals(avgSpeed4)) {
                        String text = new String(message.getPayload());
                        speed4.setText(text);
                    }
                    if (topic.equals(avgSpeed5)) {
                        String text = new String(message.getPayload());
                        speed5.setText(text);
                    }

                }

                @Override
                public void deliveryComplete(IMqttDeliveryToken token) {
                    Log.d(TAG, "Message delivered");
                }
            });
        }
    }

    public void drive(String direction, String actionDescription) {
        if (!isConnected) {
            final String notConnected = "Not connected (yet)";
            Log.e(TAG, notConnected);
            Toast.makeText(getApplicationContext(), notConnected, Toast.LENGTH_SHORT).show();
            return;
        }
        Log.i(TAG, actionDescription);
    }

}
