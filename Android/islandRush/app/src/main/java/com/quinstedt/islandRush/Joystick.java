package com.quinstedt.islandRush;

import static com.quinstedt.islandRush.BrokerConnection.Topics.Connection.QOS;
import static com.quinstedt.islandRush.BrokerConnection.Topics.Race.CONTROLLER;
import static com.quinstedt.islandRush.BrokerConnection.Topics.Race.SET_CAR_SPEED;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import com.example.joystickjhr.JoystickJhr;

public class Joystick extends AppCompatActivity {

    private int lastDirection = 0;
    private int counter = 0;
    private MqttClient mMqttClient;
    private BrokerConnection brokerConnection;
    private SpeedometerView Speed;
    private final String printSpeed = "Speed";


    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_joystick);

        brokerConnection = new BrokerConnection(getApplicationContext());
        brokerConnection.setActualSpeed(findViewById(R.id.actualSpeedJoystick));
       // brokerConnection.setmCameraView(findViewById(R.id.joystick_camera));
        mMqttClient = brokerConnection.getmMqttClient();
        brokerConnection.connectToMqttBroker();

        ImageButton escapeHash = findViewById(R.id.joystick_escapeHash);
        escapeHash.setOnClickListener((View view) -> goBack());

        Button brake = findViewById(R.id.brakeJoystick);
        brake.setOnClickListener(view -> brake());

        Button stop = findViewById(R.id.stopJoystick);
        stop.setOnClickListener(view -> stop());

        Button acceleration = findViewById(R.id.accelerateJoystick);
        acceleration.setOnClickListener(view -> acceleration());

        Button fullSpeed = findViewById(R.id.fullSpeedJoystick);
        fullSpeed.setOnClickListener(view -> setFullSpeed());


        JoystickJhr joystick = findViewById(R.id.joystick);
        /**
         * TO avoid sending the same direction multiple times when the joystick
         * is the same angle range the variable lastDirection makes sure that
         * when the joystick is in a new angle range a message is publish to the broker
         */
        joystick.setOnTouchListener((View view, MotionEvent motionEvent) -> {

            joystick.move(motionEvent);
            joystick.joyX();
            joystick.joyY();
            joystick.angle();
            joystick.distancia();

            int direction = joystick.getDireccion();

            if (lastDirection != direction) {
                lastDirection = direction;
                sendJoystickDirection(direction);
            }
            return true;
        });

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

    /**
     * Publish the direction of the joystick and the topic for the joystick control
     * @param direction - an int use by the library to define a direction
     */
    private void sendJoystickDirection(int direction) {

        switch (direction) {
            case JoystickJhr.STICK_UP:
                driveControl(Direction.FORWARD.toString(), "Moving forward");
                break;
            case JoystickJhr.STICK_UPRIGHT:
                driveControl(Direction.UPRIGHT.toString(), "Moving forward to diagonal right");
                break;
            case JoystickJhr.STICK_RIGHT:
                driveControl(Direction.RIGHT.toString(), "Moving right");
                break;
            case JoystickJhr.STICK_DOWNRIGHT:
                driveControl(Direction.BACKRIGHT.toString(), "Moving reverse to diagonal right");
                break;
            case JoystickJhr.STICK_DOWN:
                driveControl(Direction.REVERSE.toString(), "Moving in reverse");
                break;
            case JoystickJhr.STICK_DOWNLEFT:
                driveControl(Direction.BACKLEFT.toString(), "Moving reverse to diagonal left");
                break;
            case JoystickJhr.STICK_LEFT:
                driveControl(Direction.LEFT.toString(), "Moving to the left");
                break;
            case JoystickJhr.STICK_UPLEFT:
                driveControl(Direction.UPLEFT.toString(), "Moving diagonal forward left");
                break;
        }

    }

    /**
     * Method used for the escapeHash to go back to ControlChoice activity
     */
    private void goBack() {
        Intent controlChoiceActivity = new Intent(this, ControlChoice.class);
        startActivity(controlChoiceActivity);
    }

    /**
     * Send a message to the broker for a specific topic and prints a description in the android compiler
     * @param message - the message that will be send to the broker
     * @param actionDescription - the action description that will be printed
     */
    public void driveControl(String message, String actionDescription) {
        brokerConnection.drive(message,actionDescription);
        mMqttClient.publish(CONTROLLER, message, QOS, null);
    }
    public void driveSpeed(String message, String actionDescription) {
        brokerConnection.drive(message,actionDescription);
        mMqttClient.publish(SET_CAR_SPEED, message, QOS, null);
    }
    /**
        The methods below are sending the speed to arduino, changing the speedometer UI and printing the velocity
     */
    public void setFullSpeed() {
        counter = 10;
        int setSpeed = counter * 10;
        String velocityText = "Velocity: " + setSpeed;
        driveSpeed(Integer.toString(setSpeed), velocityText + ". On full speed.");
        Speed.setSpeed(setSpeed, 2000, 500);  // process: the speed wanted, duration: the animation duration, startDelay: the delay before the animation starts
        Log.i(printSpeed, velocityText);
    }
    public void brake() {
        if(counter > 0){
            counter--;
            int setSpeed = counter * 10;
            String velocityText = "Velocity: " + setSpeed;
            driveSpeed(Integer.toString(setSpeed), "Using Brake new " + velocityText);
            Speed.setSpeed(setSpeed , 2000, 500);
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
        Log.i(printSpeed, velocityText);
        driveSpeed(Integer.toString(counter),velocityText + ".Stopping");
        double stop = 0.000001;
        // The speed needs to be < 0
        Speed.setSpeed(stop,2000,500);

    }
}