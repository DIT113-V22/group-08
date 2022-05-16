package com.quinstedt.islandRush;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import com.example.joystickjhr.JoystickJhr;

public class Joystick extends AppCompatActivity {

    private int lastDirection = 0;
    private ImageButton escapeHash;
    private MqttClient mMqttClient;
    private BrokerConnection brokerConnection;
    private Button stop;


    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_joystick);

        brokerConnection = new BrokerConnection(getApplicationContext());
        brokerConnection.setActualSpeed(findViewById(R.id.actualSpeedJoystick));
        brokerConnection.setmCameraView(findViewById(R.id.joystick_camera));
        mMqttClient = brokerConnection.getmMqttClient();
        brokerConnection.connectToMqttBroker();

        escapeHash = findViewById(R.id.joystick_escapeHash);
        escapeHash.setOnClickListener((View view) -> goBack());
        stop = findViewById(R.id.stopJoystick);
        stop.setOnClickListener(view -> stopCar());


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

    }

    /**
     * Publish the direction of the joystick and the topic for the joystick control
     * @param direction - an int use by the library to define a direction
     */
    private void sendJoystickDirection(int direction) {

        switch (direction) {
            case JoystickJhr.STICK_UP:
                drive(Direction.FORWARD.toString(), "Moving forward");
                break;
            case JoystickJhr.STICK_UPRIGHT:
                drive(Direction.UPRIGHT.toString(), "Moving forward to diagonal right");
                break;
            case JoystickJhr.STICK_RIGHT:
                drive(Direction.RIGHT.toString(), "Moving right");
                break;
            case JoystickJhr.STICK_DOWNRIGHT:
                drive(Direction.BACKRIGHT.toString(), "Moving reverse to diagonal right");
                break;
            case JoystickJhr.STICK_DOWN:
                drive(Direction.REVERSE.toString(), "Moving in reverse");
                break;
            case JoystickJhr.STICK_DOWNLEFT:
                drive(Direction.BACKLEFT.toString(), "Moving reverse to diagonal left");
                break;
            case JoystickJhr.STICK_LEFT:
                drive(Direction.LEFT.toString(), "Moving to the left");
                break;
            case JoystickJhr.STICK_UPLEFT:
                drive(Direction.UPLEFT.toString(), "Moving diagonal forward left");
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
    public void drive(String message, String actionDescription) {
        brokerConnection.drive(message,actionDescription);
        mMqttClient.publish(brokerConnection.CONTROLLER, message,brokerConnection.QOS, null);

    }
    /**
        Method use to send to stop the car. The toString of the enum Stop is
        actually sending a number. See the Direction class.
     */
    private void stopCar() {
        drive(Direction.STOP.toString(),"Stopping");
    }
}