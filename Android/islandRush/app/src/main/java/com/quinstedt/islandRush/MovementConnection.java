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

public class MovementConnection extends AppCompatActivity {
    /**
     * Create setters and getter for the the ones with public and see if a composition can be done
     */
    public static final String TAG = "IslandRushApp";
    private static final String EXTERNAL_MQTT_BROKER = "aerostun.dev";
    private static final String LOCALHOST = "10.0.2.2";
    public static final String MQTT_SERVER = "tcp://" + LOCALHOST + ":1883";
    public static final int QOS = 1;
    private boolean isConnected = false;
    public MqttClient mMqttClient;

    // MQTT TOPICS
    public static final String MAIN_TOPIC = "/IslandRush";

    public static final String CAMERA = MAIN_TOPIC + "/camera";
    //Sensor topics
    // static final String ULTRASOUND = MAIN_TOPIC +  "/ultrasound/front";
    public  static final String ODOMETER_DISTANCE = MAIN_TOPIC + "/Odometer/Distance";
    public static final String ODOMETER_SPEED = MAIN_TOPIC + "/Odometer/Speed";
    //Controller topics
    public static final String CONTROLLER = MAIN_TOPIC + "/Control/Direction";
    public static final String SET_CAR_SPEED = MAIN_TOPIC + "/Control/Speed";

    public static final String SERVER = MAIN_TOPIC + "/server";
    public static final String FINISH = MAIN_TOPIC + "/Mood/Race/Finish";

    // CAMERA
    private static final int IMAGE_WIDTH = 320; //640
    private static final int IMAGE_HEIGHT = 240;//480

    ImageView mCameraView;
    TextView actualSpeed;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mMqttClient = new MqttClient(getApplicationContext(), MQTT_SERVER, TAG);
        mCameraView = findViewById(R.id.controlPad_camera);
        actualSpeed = findViewById(R.id.actualSpeed);
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
                    mMqttClient.subscribe(ODOMETER_SPEED, QOS, null);
                    mMqttClient.subscribe(ODOMETER_DISTANCE, QOS, null);
                    mMqttClient.subscribe(CAMERA, QOS, null);
                    mMqttClient.subscribe(SERVER,QOS,null);

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
                    }else {
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

    public void setActualSpeedFromString(TextView actualSpeed, String speed ) {
        @SuppressLint("DefaultLocale") String roundedSpeed = String.format("%.2f",Double.parseDouble(speed));
        actualSpeed.setText(" : " + roundedSpeed + " m/s");
    }

}
