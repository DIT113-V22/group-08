package com.quinstedt.islandRush;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;

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
    static final String MAIN_TOPIC = "/islandRush";

    static final String CAMERA = MAIN_TOPIC + "/camera";
    //Sensor topics
    static final String ULTRASOUND = MAIN_TOPIC +  "/ultrasound/front";
    static final String ODOMETER_LEFT_DISTANCE = MAIN_TOPIC + "/Odometer/LeftDistance";
    static final String ODOMETER_RIGHT_DISTANCE = MAIN_TOPIC + "/Odometer/RightDistance";
    static final String ODOMETER_LEFT_SPEED = MAIN_TOPIC + "/Odometer/LeftSpeed";
    static final String ODOMETER_RIGHT_SPEED = MAIN_TOPIC + "/Odometer/RightSpeed";
    //Controller topics
    static final String CONTROLLER = MAIN_TOPIC + "/control/direction";

    // CAMERA
     static final int IMAGE_WIDTH = 320;
     static final int IMAGE_HEIGHT = 240;
     ImageView mCameraView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mMqttClient = new MqttClient(getApplicationContext(), MQTT_SERVER, TAG);
        mCameraView = findViewById(R.id.controlPad_camera);
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
                    mMqttClient.subscribe(ULTRASOUND, QOS, null);
                    mMqttClient.subscribe(ODOMETER_LEFT_DISTANCE, QOS, null);
                    mMqttClient.subscribe(ODOMETER_RIGHT_DISTANCE, QOS, null);
                    mMqttClient.subscribe(ODOMETER_LEFT_SPEED, QOS, null);
                    mMqttClient.subscribe(ODOMETER_RIGHT_SPEED, QOS, null);
                    mMqttClient.subscribe(CAMERA, QOS, null);
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
