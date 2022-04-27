package com.quinstedt.islandRush;

import android.os.Bundle;
import android.view.View;

public class ControlPad extends MovementConnection {

    @Override
    public void onCreate(Bundle savedInstanceState ) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_control_pad);
        mMqttClient = new MqttClient(getApplicationContext(), MQTT_SERVER, TAG);
        mCameraView = findViewById(R.id.controlPad_camera);

        connectToMqttBroker();
    }
    @Override
    public void drive(String direction, String actionDescription) {
        super.drive(direction,actionDescription);
        mMqttClient.publish(CONTROLLER, direction,QOS, null);
    }
    public void moveForward(View view) {
        drive(Direction.FORWARD.toString(), "Moving forward");
    }
    public void moveForwardLeft(View view) { drive(Direction.LEFT.toString(), "Moving to the left"); }
    public void stop(View view) {
        drive(Direction.STOP.toString(),"Stopping");
    }
    public void moveForwardRight(View view) { drive(Direction.RIGHT.toString(), "Moving right"); }
    public void moveBackward(View view) {
        drive(Direction.REVERSE.toString(), "Moving in reverse");
    }
}