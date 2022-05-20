package com.quinstedt.islandRush;

import static com.quinstedt.islandRush.Topics.CAMERA;
import static com.quinstedt.islandRush.Topics.Connection.MQTT_SERVER;
import static com.quinstedt.islandRush.Topics.Connection.QOS;
import static com.quinstedt.islandRush.Topics.Connection.TAG;
import static com.quinstedt.islandRush.Topics.Sensor.ODOMETER_DISTANCE;
import static com.quinstedt.islandRush.Topics.Sensor.ODOMETER_SPEED;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;


import android.os.Bundle;
import android.os.SystemClock;

import android.util.Log;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;


import java.util.HashMap;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;


public class BrokerConnection  {

    private boolean isConnected = false;
    public MqttClient mMqttClient;

    // MQTT TOPICS
    public static final String MAIN_TOPIC = "/IslandRush";

    public static final String CAMERA = MAIN_TOPIC + "/camera";
    //Sensor topics

    public  static final String ODOMETER_DISTANCE = MAIN_TOPIC + "/Odometer/Distance";
    public static final String ODOMETER_SPEED = MAIN_TOPIC + "/Odometer/Speed";
    //Controller topics
    public static final String CONTROLLER = MAIN_TOPIC + "/Control/Direction";
    public static final String  SET_CAR_SPEED = MAIN_TOPIC + "/Control/Speed";

    public static final String SERVER = MAIN_TOPIC + "/server";

    public static final String FINISH = MAIN_TOPIC + "/Mood/Race/Finish";


    // CAMERA
    private static final int IMAGE_WIDTH = 320;
    private static final int IMAGE_HEIGHT = 240;

    ImageView mCameraView;
    TextView actualSpeed;
    Context context;
    TextView finish;
    Chronometer simpleChronometer;
    TextView t;

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
                    }else if(topic.equals(FINISH)){
                            String finishMessage = "Finish";
                            finish.setText(finishMessage);

                            simpleChronometer.stop();
                            long elapsedMillis = SystemClock.elapsedRealtime() - simpleChronometer.getBase();


                            DateFormat simple = new SimpleDateFormat("mm:ss.SSS");


                            Date result = new Date(elapsedMillis);


                            t.setText(String.valueOf(simple.format(result)));

                        }else {
                        Log.i(TAG, "[MQTT] Topic: " + topic + " | Message: " + message.toString());
                    }
               /* String messageMQTT;
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
                       default:
                           Log.i(TAG, "[MQTT] Topic: " + topic + " | Message: " + message.toString());**/
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

        public void setFinish(TextView finish) {
            this.finish = finish;
        }

        public void setSimpleChronometer(Chronometer simpleChronometer) {
            this.simpleChronometer = simpleChronometer;
        }

        public void setT(TextView t) {
            this.t = t;
        }
}
