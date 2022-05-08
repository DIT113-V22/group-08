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

public class Joystick extends MovementConnection {
    int lastDirection = 0;
    ImageButton escapeHash;
    private Button stop;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_joystick);
        mMqttClient = new MqttClient(getApplicationContext(), MQTT_SERVER, TAG);
        mCameraView = findViewById(R.id.joystick_camera);
        actualSpeed = findViewById(R.id.actualSpeedJoystick);
        // Start
        escapeHash = findViewById(R.id.joystick_escapeHash);
        escapeHash.setOnClickListener((View view) -> goBack());
        stop = findViewById(R.id.stopJoystick);
        stop.setOnClickListener(view -> stopCar());


        JoystickJhr joystick = findViewById(R.id.joystick);

        joystick.setOnTouchListener((View view, MotionEvent motionEvent) -> {

            joystick.move(motionEvent);
            joystick.joyX();
            joystick.joyY();
            joystick.angle();
            joystick.distancia();

            int direction = joystick.getDireccion();

            if (lastDirection != direction) {
                lastDirection = direction;

                if (direction == JoystickJhr.STICK_UP) {
                    drive(Direction.FORWARD.toString(), "Moving forward");
                } else if (direction == JoystickJhr.STICK_UPRIGHT) {
                    drive(Direction.UPRIGHT.toString(), "Moving forward to diagonal right");
                } else if (direction == JoystickJhr.STICK_RIGHT) {
                    drive(Direction.RIGHT.toString(), "Moving right");
                } else if (direction == JoystickJhr.STICK_DOWNRIGHT) {
                    drive(Direction.BACKRIGHT.toString(), "Moving reverse to diagonal right");
                } else if (direction == JoystickJhr.STICK_DOWN) {
                    drive(Direction.REVERSE.toString(), "Moving in reverse");
                } else if (direction == JoystickJhr.STICK_DOWNLEFT) {
                    drive(Direction.BACKLEFT.toString(), "Moving reverse to diagonal left");
                } else if (direction == JoystickJhr.STICK_LEFT) {
                    drive(Direction.LEFT.toString(), "Moving to the left");
                } else if (direction == JoystickJhr.STICK_UPLEFT) {
                    drive(Direction.UPLEFT.toString(), "Moving diagonal forward left");
                }

            }

            return true;
        });

    }

    private void goBack() {
        Intent controlChoiceActivity = new Intent(this, ControlChoice.class);
        startActivity(controlChoiceActivity);
    }


    @Override
    public void drive(String direction, String actionDescription) {
        super.drive(direction,actionDescription);
        mMqttClient.publish(CONTROLLER, direction,QOS, null);
    }
    private void stopCar() {
        drive(Direction.STOP.toString(),"Stopping");
    }


}