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

    public static class Topics {


        public static final String MAIN_TOPIC = "/IslandRush";

        public static class Connection{
            public static final String TAG = "IslandRushApp";
            public static final String EXTERNAL_MQTT_BROKER = "aerostun.dev";
            public static final String LOCALHOST = "10.0.2.2";
            public static final String MQTT_SERVER = "tcp://" + LOCALHOST + ":1883";
            public static final int QOS = 1;
        }
        public static class Sensor{
            public static final String ODOMETER_DISTANCE = MAIN_TOPIC + "/Odometer/Distance";
            public static final String ODOMETER_SPEED = MAIN_TOPIC + "/Odometer/Speed";
        }
        public static class Race {
            public static final String CONTROLLER_JOYSTICK = MAIN_TOPIC + "/Control/Joystick/Direction";
            public static final String CONTROLLER_CONTROLPAD = MAIN_TOPIC + "/Control/ControlPad/Direction";
            public static final String SET_CAR_SPEED = MAIN_TOPIC + "/Control/Speed";
            public static final String FINISH = MAIN_TOPIC + "/Mood/Race/Finish";
        }

    }

    private boolean isConnected = false;
    public MqttClient mMqttClient;

    TextView actualSpeed;
    Context context;
    TextView finish;
    Chronometer simpleChronometer;
    TextView t;


    public  BrokerConnection(Context context){
        this.context = context;
        mMqttClient = new MqttClient(context, Topics.Connection.MQTT_SERVER,Topics.Connection.TAG);

        connectToMqttBroker();
    }

    public void connectToMqttBroker() {
        if (!isConnected) {
            mMqttClient.connect(Topics.Connection.TAG, "", new IMqttActionListener() {
                /**
                 *  Add below the topic that the app subscribe to
                 *  and add the method for that topic in messageArrived(...)
                 */
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    isConnected = true;
                    final String successfulConnection = "Connected to MQTT broker";
                    Log.i(Topics.Connection.TAG, successfulConnection);
                    Toast.makeText(context, successfulConnection, Toast.LENGTH_SHORT).show();

                    mMqttClient.subscribe(Topics.Sensor.ODOMETER_SPEED,Topics.Connection.QOS, null);
                    mMqttClient.subscribe(Topics.Sensor.ODOMETER_DISTANCE, Topics.Connection.QOS, null);
                    mMqttClient.subscribe(Topics.Race.FINISH,Topics.Connection.QOS,null);
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    final String failedConnection = "Failed to connect to MQTT broker";
                    Log.e(Topics.Connection.TAG, failedConnection);
                    Toast.makeText(context, failedConnection, Toast.LENGTH_SHORT).show();
                }
            }, new MqttCallback() {
                @Override
                public void connectionLost(Throwable cause) {
                    isConnected = false;

                    final String connectionLost = "Connection to MQTT broker lost";
                    Log.w(Topics.Connection.TAG, connectionLost);
                    Toast.makeText(context, connectionLost, Toast.LENGTH_SHORT).show();
                }
                /**
                 *  Method that retrieve the message inside a topic
                 *
                 *  Note: change to a switch instead for better structure
                 */
                @Override
                public void messageArrived(String topic, MqttMessage message) throws Exception {

                    if(topic.equals(Topics.Sensor.ODOMETER_DISTANCE)){
                        String messageMQTT = message.toString();
                        Log.i(Topics.Connection.TAG, "Car distance" + messageMQTT);
                    }else if(topic.equals(Topics.Sensor.ODOMETER_SPEED)) {
                        String messageMQTT = message.toString();
                        setActualSpeedFromString(actualSpeed, messageMQTT);
                        Log.i(Topics.Connection.TAG, "Car speed: " + messageMQTT);

                    }else if(topic.equals(Topics.Race.FINISH)){

                            String finishMessage = "Finish";
                            finish.setText(finishMessage);
                            simpleChronometer.stop();
                            long elapsedMillis = SystemClock.elapsedRealtime() - simpleChronometer.getBase();
                            DateFormat simple = new SimpleDateFormat("mm:ss.SSS");
                            Date result = new Date(elapsedMillis);
                            t.setText(String.valueOf(simple.format(result)));

                        }else {
                        Log.i(Topics.Connection.TAG, "[MQTT] Topic: " + topic + " | Message: " + message.toString());

                    }                               

                @Override
                public void deliveryComplete(IMqttDeliveryToken token) {
                    Log.d(Topics.Connection.TAG, "Message delivered");
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
            Log.e(Topics.Connection.TAG, notConnected);
            Toast.makeText(context, notConnected, Toast.LENGTH_SHORT).show();
            return;
        }
        Log.i(Topics.Connection.TAG, actionDescription);
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
