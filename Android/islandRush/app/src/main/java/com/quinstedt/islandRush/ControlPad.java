package com.quinstedt.islandRush;

import static com.quinstedt.islandRush.BrokerConnection.Topics.Connection.QOS;
import static com.quinstedt.islandRush.BrokerConnection.Topics.Race.CONTROLLER;
import static com.quinstedt.islandRush.BrokerConnection.Topics.Race.SET_CAR_SPEED;


import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;


import androidx.appcompat.app.AppCompatActivity;

public class ControlPad extends AppCompatActivity {
    private BrokerConnection brokerConnection;
    private MqttClient mMqttClient;
    private SpeedometerView Speed;
    int counter = 0;
    private final String printSpeed = "Speed";


    @Override
    public void onCreate(Bundle savedInstanceState ) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_control_pad);

        brokerConnection = new BrokerConnection(getApplicationContext());
        brokerConnection.setActualSpeed(findViewById(R.id.actualSpeed));
      //  brokerConnection.setmCameraView(findViewById(R.id.controlPad_camera));
        brokerConnection.setFinish(findViewById(R.id.finish_controlPad));
        mMqttClient = brokerConnection.getmMqttClient();
        brokerConnection.connectToMqttBroker();

        ImageButton escapeHash = findViewById(R.id.controlPad_escapeHash);
        escapeHash.setOnClickListener(view -> goBack());

        Button brake = findViewById(R.id.brakeControlPad);
        brake.setOnClickListener(view -> brake());

        Button stop = findViewById(R.id.stop);
        stop.setOnClickListener(view -> stop());

        Button acceleration = findViewById(R.id.accelerateControlPad);
        acceleration.setOnClickListener(view -> acceleration());

        Button fullSpeed = findViewById(R.id.fullSpeedControlPad);
        fullSpeed.setOnClickListener(view -> setFullSpeed());


        Speed =  findViewById(R.id.speedometerControlPad);
        Speed.setLabelConverter((progress, maxProgress) -> String.valueOf((int) Math.round(progress)));

// configure value range and ticks in the UI
        Speed.setMaxSpeed(100);
        Speed.setMajorTickStep(20);
        Speed.setMinorTicks(0);

// Configure value range colors in the UI
        Speed.addColoredRange(0, 50, Color.GREEN);
        Speed.addColoredRange(50, 75, Color.YELLOW);
        Speed.addColoredRange(75, 100, Color.RED);

    }

    public void setFullSpeed() {
        counter = 10;
        int setSpeed = counter * 10;
        String velocityText = "Velocity: " + setSpeed;
        driveSpeed(Integer.toString(setSpeed), "Setting velocity on full speed");
        Speed.setSpeed(setSpeed, 2000, 500);
        Log.i(printSpeed, velocityText);
    }
    public void brake() {
        if(counter > 0){
            counter--;
            int setSpeed = counter * 10;
            String velocityText = "Velocity: " + setSpeed;
            driveSpeed(Integer.toString(setSpeed), "Using Brake new " + velocityText);
            Speed.setSpeed(setSpeed, 2000, 500);
            Log.i(printSpeed, velocityText);
        }
    }
    public void acceleration() {
        if(counter < 10){
            counter ++;
            int setSpeed = counter * 10;
            String velocityText = "Velocity: " + setSpeed;
            driveSpeed(Integer.toString(setSpeed), "Using Acceleration new " + velocityText);
            Speed.setSpeed(setSpeed , 2000, 500);
            Log.i(printSpeed, velocityText);
        }
    }
    public void stop() {
        counter = 0;
        String velocityText = "Velocity: " + counter;
        driveSpeed(Integer.toString(counter),"Stopping");
        double stop = 0.000001;
        Speed.setSpeed(stop,2000,500);
        // The speed needs to be < 0
        Log.i(printSpeed, velocityText);
    }

    /**
     * Launches the ControlChoice after that the escape Hash has been clicked
     */
    private void goBack() {
        Intent controlChoiceActivity = new Intent(this, ControlChoice.class);
        startActivity(controlChoiceActivity);
    }

    /**
     * See BrokerConnection.
     * @param message - the message that we send to the broker
     * @param actionDescription - the action description that will be printed
     */

    public void driveControl(String message, String actionDescription) {
        brokerConnection.drive(message,actionDescription);
        mMqttClient.publish(CONTROLLER, message,QOS, null);
    }
    public void driveSpeed(String message, String actionDescription) {
        brokerConnection.drive(message,actionDescription);
        mMqttClient.publish(SET_CAR_SPEED, message,QOS, null);
    }

    /**
     * The methods below are also publishing the direction to the broker.
     * The difference here is that this method are connected directly to the activity
     * in this case ControlPad, where every method has been assigned by using
     * to each button using " android:onClick="<method>" " to connected them
     *
     * @param view - The activity that has been used
     */
    public void moveForward(View view) {
        driveControl(Direction.FORWARD.toString(), "Moving forward");
    }
    public void moveForwardLeft(View view) {
        driveControl(Direction.LEFT.toString(), "Moving to the left");
    }

    public void moveForwardRight(View view) {
        driveControl(Direction.RIGHT.toString(), "Moving right");
    }
    public void moveBackward(View view) {
        driveControl(Direction.REVERSE.toString(), "Moving in reverse");
    }

}