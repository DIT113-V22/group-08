package com.quinstedt.islandRush;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class ControlPad extends MovementConnection {
    ImageButton escapeHash;
    @Override
    public void onCreate(Bundle savedInstanceState ) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_control_pad);
        mMqttClient = new MqttClient(getApplicationContext(), MQTT_SERVER, TAG);
        actualSpeed = findViewById(R.id.actualSpeed);
        mCameraView = findViewById(R.id.controlPad_camera);

        connectToMqttBroker();

        // start timer
        escapeHash = findViewById(R.id.controlPad_escapeHash);
        escapeHash.setOnClickListener(view -> goBack());
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
    public void moveForward(View view) {
        drive(Direction.FORWARD.toString(), "Moving forward");
    }
    public void moveForwardLeft(View view) {
        drive(Direction.LEFT.toString(), "Moving to the left");
    }
    public void stop(View view) {
        drive(Direction.STOP.toString(),"Stopping");
    }
    public void moveForwardRight(View view) {
        drive(Direction.RIGHT.toString(), "Moving right");
    }
    public void moveBackward(View view) {
        drive(Direction.REVERSE.toString(), "Moving in reverse");
    }
}