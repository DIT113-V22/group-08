package com.quinstedt.islandRush;

import static com.quinstedt.islandRush.Topics.Connection.QOS;
import static com.quinstedt.islandRush.Topics.Controller.CONTROLLER;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import android.widget.Chronometer;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class ControlPad extends AppCompatActivity {
    ImageButton escapeHash;
    BrokerConnection brokerConnection;
    MqttClient mMqttClient;
    Button saveScoreScreen;
    Chronometer simpleChronometer;


    @Override
    public void onCreate(Bundle savedInstanceState ) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_control_pad);

        brokerConnection = new BrokerConnection(getApplicationContext());
        brokerConnection.setActualSpeed(findViewById(R.id.actualSpeed));
        brokerConnection.setmCameraView(findViewById(R.id.controlPad_camera));

        brokerConnection.setFinish(findViewById(R.id.finish_controlPad));
        brokerConnection.setSimpleChronometer(findViewById(R.id.simpleChronometerControlPad));
        brokerConnection.setT(findViewById(R.id.TOTALTIME_ControlPad));
        mMqttClient = brokerConnection.getmMqttClient();

        brokerConnection.connectToMqttBroker();
        mMqttClient = brokerConnection.getmMqttClient();


        // start timer
        simpleChronometer = (Chronometer) findViewById(R.id.simpleChronometerControlPad);

        simpleChronometer.start();

        TextView timerView = (TextView) findViewById(R.id.TOTALTIME_ControlPad);

        escapeHash = findViewById(R.id.controlPad_escapeHash);
        escapeHash.setOnClickListener(view -> goBack());

        saveScoreScreen=findViewById(R.id.saveScoreBtn);
        saveScoreScreen.setOnClickListener(view -> goToSaveScoreScreen());
    }

    /**
     * Launches the ControlChoice after that the escape Hash has been clicked
     */
    private void goBack() {
        Intent controlChoiceActivity = new Intent(this, MainActivity.class);
        startActivity(controlChoiceActivity);
    }

    private void goToSaveScoreScreen() {
        Intent controlChoiceActivity = new Intent(this, SaveScore.class);
        startActivity(controlChoiceActivity);
    }

    /**
     * See BrokerConnection.
     * @param message - the message that we send to the broker
     * @param actionDescription - the action description that will be printed
     */

    public void drive(String message, String actionDescription) {
        brokerConnection.drive(message,actionDescription);
        mMqttClient.publish(CONTROLLER, message,QOS, null);

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