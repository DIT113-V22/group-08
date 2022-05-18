package com.quinstedt.islandRush;

import static com.quinstedt.islandRush.Topics.CAMERA;
import static com.quinstedt.islandRush.Topics.Connection.MQTT_SERVER;
import static com.quinstedt.islandRush.Topics.Connection.QOS;
import static com.quinstedt.islandRush.Topics.Connection.TAG;
import static com.quinstedt.islandRush.Topics.Sensor.ODOMETER_DISTANCE;
import static com.quinstedt.islandRush.Topics.Sensor.ODOMETER_SPEED;
import static com.quinstedt.islandRush.Topics.Server.SERVER;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.util.HashMap;

public class BrokerConnection  {

    private boolean isConnected = false;
    public MqttClient mMqttClient;

    // CAMERA
    private static final int IMAGE_WIDTH = 320;
    private static final int IMAGE_HEIGHT = 240;

    private static final String SERVER_TOPICS =  SERVER + "/#";
    ImageView mCameraView;
    TextView actualSpeed;
    Context context;
    HashMap<String, TextView> leaderboard;

    TextView player1;
    TextView player2;
    TextView player3;
    TextView player4;
    TextView player5;

    TextView time1;
    TextView time2;
    TextView time3;
    TextView time4;
    TextView time5;

    TextView L2player1;
    TextView L2player2;
    TextView L2player3;
    TextView L2player4;
    TextView L2player5;

    TextView avgSpeed1;
    TextView avgSpeed2;
    TextView avgSpeed3;
    TextView avgSpeed4;
    TextView avgSpeed5;

    public  BrokerConnection(Context context){
        this.context = context;
        mMqttClient = new MqttClient(this.context, MQTT_SERVER,TAG);
        connectToMqttBroker();
    }

    public void connectToMqttBroker() {
        if (!isConnected) {
            mMqttClient.connect(TAG, "", new IMqttActionListener() {
                /**
                 *  Add below the topic that the app subscribe to
                 *  and add the method for that topic in messageArrived(...)
                 */
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    isConnected = true;
                    final String successfulConnection = "Connected to MQTT broker";
                    Log.i(TAG, successfulConnection);
                    Toast.makeText(context, successfulConnection, Toast.LENGTH_SHORT).show();
                    mMqttClient.subscribe(ODOMETER_SPEED, QOS, null);
                    mMqttClient.subscribe(ODOMETER_DISTANCE, QOS, null);
                    mMqttClient.subscribe(CAMERA, QOS, null);
                    mMqttClient.subscribe(Topics.Server.Leaderboard1 + "/#", QOS, null);
                    mMqttClient.subscribe(Topics.Server.Leaderboard2 + "/#", QOS, null);
                    mMqttClient.subscribe(Topics.Server.Time + "/#", QOS, null);
                    mMqttClient.subscribe(Topics.Server.AvgSpeed + "/#", QOS, null);

                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    final String failedConnection = "Failed to connect to MQTT broker";
                    Log.e(TAG, failedConnection);
                    Toast.makeText(context, failedConnection, Toast.LENGTH_SHORT).show();
                }
            }, new MqttCallback() {
                @Override
                public void connectionLost(Throwable cause) {
                    isConnected = false;

                    final String connectionLost = "Connection to MQTT broker lost";
                    Log.w(TAG, connectionLost);
                    Toast.makeText(context, connectionLost, Toast.LENGTH_SHORT).show();
                }
                /**
                 *  Method that retrieve the message inside a topic
                 *
                 *  Note: change to a switch instead for better structure
                 */
                @Override
                public void messageArrived(String topic, MqttMessage message) throws Exception {
                  /*
                    if (topic.equals(CAMERA)) {
                        final Bitmap bm = Bitmap.createBitmap(IMAGE_WIDTH, IMAGE_HEIGHT, Bitmap.Config.ARGB_8888);

                        final byte[] payload = message.getPayload();
                        final int[] colors = new int[IMAGE_WIDTH * IMAGE_HEIGHT];
                        for (int ci = 0; ci < colors.length; ++ci) {
                            final int r = payload[3 * ci] & 0xFF;
                            final int g = payload[3 * ci + 1] & 0xFF;
                            final int b = payload[3 * ci + 2] & 0xFF;
                            colors[ci] = Color.rgb(r, g, b);
                        }
                        bm.setPixels(colors, 0, IMAGE_WIDTH, 0, 0, IMAGE_WIDTH, IMAGE_HEIGHT);
                        mCameraView.setImageBitmap(bm);
                    }else if(topic.equals(ODOMETER_DISTANCE)){
                        String messageMQTT = message.toString();
                        Log.i(TAG, "Car distance" + messageMQTT);
                    }else if(topic.equals(ODOMETER_SPEED)) {
                        String messageMQTT = message.toString();
                        setActualSpeedFromString(actualSpeed, messageMQTT);
                        Log.i(TAG, "Car speed: " + messageMQTT);

                    }else if (topic.contains(SERVER)) {
                        String payload = new String(message.getPayload());
                        player1.setText(payload);

                   */

                    String messageMQTT;

                    switch (topic){
                       case CAMERA:
                           final Bitmap bm = Bitmap.createBitmap(IMAGE_WIDTH, IMAGE_HEIGHT, Bitmap.Config.ARGB_8888);

                           final byte[] payload = message.getPayload();
                           final int[] colors = new int[IMAGE_WIDTH * IMAGE_HEIGHT];
                           for (int ci = 0; ci < colors.length; ++ci) {
                               final int r = payload[3 * ci] & 0xFF;
                               final int g = payload[3 * ci + 1] & 0xFF;
                               final int b = payload[3 * ci + 2] & 0xFF;
                               colors[ci] = Color.rgb(r, g, b);
                           }
                           bm.setPixels(colors, 0, IMAGE_WIDTH, 0, 0, IMAGE_WIDTH, IMAGE_HEIGHT);
                           mCameraView.setImageBitmap(bm);
                           break;
                       case ODOMETER_DISTANCE:
                           messageMQTT = message.toString();
                           Log.i(TAG, "Car distance" + messageMQTT);
                           break;
                       case ODOMETER_SPEED:
                           messageMQTT = message.toString();
                           setActualSpeedFromString(actualSpeed, messageMQTT);
                           Log.i(TAG, "Car speed: " + messageMQTT);
                           break;
                       case  SERVER_TOPICS:
                           setup();
                           messageMQTT = message.toString();
                           leaderboard.get(topic).setText(messageMQTT);
                           Log.i(TAG, "Leaderboard: Topic: " + topic + "Message:" + messageMQTT);
                           break;

                        //case Topics.Server.playerName1:
                          // String payloadM = new String(message.getPayload());
                          // player1.setText("TESTNICOLE");
                        default:
                            Log.i(TAG, "[MQTT] Topic: " + topic + " | Message: " + message.toString());


                    //}else if(topic.equals(Topics.Server.playerName1)){
                      //  player1.setText("TEST");
                    //}else {
                    //Log.i(TAG, "[MQTT] Topic: " + topic + " | Message: " + message.toString());
                }
                }

                @Override
                public void deliveryComplete(IMqttDeliveryToken token) {
                    Log.d(TAG, "Message delivered");
                }
            });
        }
    }

    /**
     * This method is use to create the message that will be publish
     * add the mMqttClient.publish(<topic>, <message >, QOS, null);
     * to specify the topic as in ControlPad or Joystick Class
     *
     * @param message - the message that we send to the broker
     * @param actionDescription - the action description that will be printed
     */
    public void drive(String message, String actionDescription) {
        if (!isConnected) {
            final String notConnected = "Not connected (yet)";
            Log.e(TAG, notConnected);
            Toast.makeText(context, notConnected, Toast.LENGTH_SHORT).show();
            return;
        }
        Log.i(TAG, actionDescription);
    }

    /**
     * Rounds the speed send from the emulator to two decimal and sets
     * the textview that will be displayed on the app.
     *
     * @param actualSpeed - the textview
     * @param speed - the Mqtt message converted to a String
     * @return
     */
    public TextView setActualSpeedFromString(TextView actualSpeed,String speed ) {
        String roundedSpeed = String.format("%.2f",Double.parseDouble(speed));
        actualSpeed.setText(" : " + roundedSpeed + " m/s");
        return actualSpeed;
    }

    public void setmCameraView(ImageView mCameraView) {
        this.mCameraView = mCameraView;
    }

    public void setActualSpeed(TextView actualSpeed) {
        this.actualSpeed = actualSpeed;
    }

    public MqttClient getmMqttClient() {
        return mMqttClient;
    }

    public void setup(){
        String message ="message";
        player1.setText(message);
        leaderboard.put(Topics.Server.playerName1, player1);
        leaderboard.put(Topics.Server.playerName2,player2);
        leaderboard.put(Topics.Server.playerName3,player3);
        leaderboard.put(Topics.Server.playerName4,player4);
        leaderboard.put(Topics.Server.playerName5,player5);

        leaderboard.put(Topics.Server.raceTime1,time1);
        leaderboard.put(Topics.Server.raceTime2,time2);
        leaderboard.put(Topics.Server.raceTime3,time3);
        leaderboard.put(Topics.Server.raceTime4,time4);
        leaderboard.put(Topics.Server.raceTime5,time5);


        leaderboard.put(Topics.Server.L2playerName1,L2player1);
        leaderboard.put(Topics.Server.L2playerName2,L2player2);
        leaderboard.put(Topics.Server.L2playerName3,L2player3);
        leaderboard.put(Topics.Server.L2playerName4,L2player4);
        leaderboard.put(Topics.Server.L2playerName5,L2player5);

        leaderboard.put(Topics.Server.averageSpeed1,avgSpeed1);
        leaderboard.put(Topics.Server.averageSpeed2,avgSpeed2);
        leaderboard.put(Topics.Server.averageSpeed3,avgSpeed3);
        leaderboard.put(Topics.Server.averageSpeed4,avgSpeed4);
        leaderboard.put(Topics.Server.averageSpeed5,avgSpeed5);



    }

    public void setPlayer1(TextView player1) {
        this.player1 = player1;
    }

    public void setPlayer2(TextView player2) {
        this.player2 = player2;
    }

    public void setPlayer3(TextView player3) {
        this.player3 = player3;
    }

    public void setPlayer4(TextView player4) {
        this.player4 = player4;
    }

    public void setPlayer5(TextView player5) {
        this.player5 = player5;
    }

    public void setTime1(TextView time1) {
        this.time1 = time1;
    }

    public void setTime2(TextView time2) {
        this.time2 = time2;
    }

    public void setTime3(TextView time3) {
        this.time3 = time3;
    }

    public void setTime4(TextView time4) {
        this.time4 = time4;
    }

    public void setTime5(TextView time5) {
        this.time5 = time5;
    }

    public void setAvgSpeed1(TextView avgSpeed1) {
        this.avgSpeed1 = avgSpeed1;
    }

    public void setAvgSpeed2(TextView avgSpeed2) {
        this.avgSpeed2 = avgSpeed2;
    }

    public void setAvgSpeed3(TextView avgSpeed3) {
        this.avgSpeed3 = avgSpeed3;
    }

    public void setAvgSpeed4(TextView avgSpeed4) {
        this.avgSpeed4 = avgSpeed4;
    }

    public void setAvgSpeed5(TextView avgSpeed5) {
        this.avgSpeed5 = avgSpeed5;
    }

    public void setL2player1(TextView l2player1) {
        L2player1 = l2player1;
    }

    public void setL2player2(TextView l2player2) {
        L2player2 = l2player2;
    }

    public void setL2player3(TextView l2player3) {
        L2player3 = l2player3;
    }

    public void setL2player4(TextView l2player4) {
        L2player4 = l2player4;
    }

    public void setL2player5(TextView l2player5) {
        L2player5 = l2player5;
    }
}
