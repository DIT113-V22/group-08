package com.quinstedt.islandRush;

import static com.quinstedt.islandRush.BrokerConnection.Topics.Connection.QOS;
import static com.quinstedt.islandRush.BrokerConnection.Topics.Race.CONTROLLER_CONTROLPAD;
import static com.quinstedt.islandRush.BrokerConnection.Topics.Race.SET_CAR_SPEED;


import android.annotation.SuppressLint;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import android.util.Log;
import android.view.MotionEvent;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Chronometer;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class ControlPad extends AppCompatActivity {

    private BrokerConnection brokerConnection;
    private MqttClient mMqttClient;
    private SpeedometerView speedometer;
    private int counter;
    private Boolean onReverse = false;
    TextView directionIndicator;

    private final int DURATION = 2000;
    private final int DELAY = 500;
    private final int CHANGE_DIRECTION_DELAY = 100;
    private final String REVERSE_IS_ON = "REVERSE ON";
    private final String REVERSE_IS_OFF = "REVERSE OFF";
    private final int SPEED = 10;

    private final int MAX_COUNTER = 10;
    private final double STOP = 0.0000000001; // must be a number close to zero, because the speedometer doest allow the value 0

    private final String FORWARD = "1";
    private final String RIGHT = "2";
    private final String REVERSE = "3";
    private final String LEFT = "4";



    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onCreate(Bundle savedInstanceState ) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_control_pad);
        counter = 1;

        brokerConnection = new BrokerConnection(getApplicationContext());
        brokerConnection.setActualSpeed(findViewById(R.id.actualSpeed));
        brokerConnection.setFinish(findViewById(R.id.finish_controlPad));
        brokerConnection.setSimpleChronometer(findViewById(R.id.simpleChronometerControlPad));
        brokerConnection.setT(findViewById(R.id.TOTALTIME_ControlPad));
        mMqttClient = brokerConnection.getmMqttClient();

        brokerConnection.connectToMqttBroker();
        mMqttClient = brokerConnection.getmMqttClient();

        /** Start timer */
        Chronometer simpleChronometer = findViewById(R.id.simpleChronometerControlPad);

        simpleChronometer.start();
        ImageButton escapeHash = findViewById(R.id.controlPad_escapeHash);
        escapeHash.setOnClickListener(view -> goBack());

        Button forward = findViewById(R.id.up);
        forward.setOnClickListener(view -> moveForward());

        Button reverse = findViewById(R.id.down);
        reverse.setOnClickListener(view -> moveBackward());

        Button left = findViewById(R.id.left);
        /**
         * ACTION_DOWN is for when the button is press, the car moves to the left
         * ACTION_UP is for when the button is realese making the car continue forward
         * and it stops rotating to the left.
         *
         * same as for the right button below.
         */
        left.setOnTouchListener((view, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                moveForwardLeft();
            } else if (event.getAction() == MotionEvent.ACTION_UP) {
                int outputSpeed = counter * SPEED;
                if(onReverse){
                    driveControl(REVERSE, "Moving forward");
                    setCarSpeed(-outputSpeed, "Continue backwards");
                    speedometer.setSpeed(outputSpeed, DURATION, 100);
                }else {
                    driveControl(FORWARD, "Moving forward");
                    setCarSpeed(outputSpeed, "Continue  forward");
                    speedometer.setSpeed(outputSpeed, DURATION, 100);
                }
            }
            return true;
        });

        Button right = findViewById(R.id.right);

        /**
         * see the comment above for the left button.
         */
        right.setOnTouchListener((view, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
               moveForwardRight();
            } else if (event.getAction() == MotionEvent.ACTION_UP) {
                int outputSpeed = counter * SPEED;
                if(onReverse){
                    driveControl(REVERSE, "Moving forward");
                    setCarSpeed(-counter * SPEED, "Continue backwards");
                    speedometer.setSpeed(counter * SPEED, DURATION, 100);
                }else {
                    driveControl(FORWARD, "Moving forward");
                    setCarSpeed(outputSpeed, "Continue  forward");
                    speedometer.setSpeed(outputSpeed, DURATION, 100);
                }
            }
            return true;
        });

        directionIndicator = findViewById(R.id.direction_indicator);


        Button brake = findViewById(R.id.brakeControlPad);
        brake.setOnClickListener(view -> brake());

        Button stop = findViewById(R.id.stop);
        stop.setOnClickListener(view -> stop());

        Button acceleration = findViewById(R.id.accelerateControlPad);
        acceleration.setOnClickListener(view -> acceleration());

        Button fullSpeed = findViewById(R.id.fullSpeedControlPad);
        fullSpeed.setOnClickListener(view -> setFullSpeed());

        speedometer =  findViewById(R.id.speedometerControlPad);
        speedometer.setLabelConverter((progress, maxProgress) -> String.valueOf((int) Math.round(progress)));

        /**
         * Configure value range and ticks in the UI
         */
        speedometer.setMaxSpeed(100);
        speedometer.setMajorTickStep(20);
        speedometer.setMinorTicks(0);

        /**
         *  Configure value range colors in the UI
         */

        speedometer.addColoredRange(0, 50, Color.GREEN);
        speedometer.addColoredRange(50, 75, Color.YELLOW);
        speedometer.addColoredRange(75, 100, Color.RED);

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

    /** One drive method for the movement of the car and one for the speed
     * See BrokerConnection.
     * @param message - the message that we send to the broker
     * @param actionDescription - the action description that will be printed
     */

    public void driveControl(String message, String actionDescription) {
        brokerConnection.drive(message,actionDescription);
        mMqttClient.publish(CONTROLLER_CONTROLPAD, message,QOS, null);
    }

    public void driveSpeed(String message, String actionDescription) {
        brokerConnection.drive(message,actionDescription);
        mMqttClient.publish(SET_CAR_SPEED, message,QOS, null);
    }


    public void setCarSpeed(int speed, String description ){
        String velocityText = "Velocity: " + speed;
        driveSpeed(Integer.toString(speed),description + velocityText);
        String printSpeed = "Speed: ";
        Log.i(printSpeed, velocityText);


    }

    /**
     * Sets the car to full speed, depending on if in reverse is active to make sure
     * that if we are on reverse the cars can continue in that direction.
     * Also sets the speedometer UI to the max value
     */
    public void setFullSpeed() {
        counter = MAX_COUNTER;
        int outPutSpeed = counter * SPEED;
        if(onReverse){
            setCarSpeed(-outPutSpeed, "Setting velocity on full speed. ");
        }else{
            setCarSpeed(outPutSpeed, "Setting velocity on full speed. ");
        }
        speedometer.setSpeed(outPutSpeed, DURATION, DELAY);
    }

    /**
     * This method decreases the speed of the car taking into account if onReverse is on or off
     * and sets the speedometer UI to the new speed value
     */
    public void brake() {
        if (counter == 1) {
            stop();
        } else if (counter > 1){
            counter--;
            int outputSpeed = counter * SPEED;

            if(onReverse){
                setCarSpeed(-outputSpeed, "Using Reverse break. ");
            } else{
                setCarSpeed(outputSpeed, "Using forward break. " );
            }

            speedometer.setSpeed(outputSpeed, DURATION, DELAY);
        }
    }

    /**
     * This method increases the speed of the car and sets the speedometor UI to the new value
     * also taking into account the if onReverse is on or off.
     */
    public void acceleration() {
        if (counter == 0 && !onReverse){
            setUpDirectionIndicator(REVERSE_IS_OFF);
        }

        if (counter < MAX_COUNTER) {
            counter++;
            int outputSpeed = counter * SPEED;

            if(onReverse){
                setCarSpeed(-outputSpeed, "Using Reverse break. ");
            } else{
                setCarSpeed(outputSpeed, "Using forward break. " );
            }
            speedometer.setSpeed(outputSpeed, DURATION, DELAY);
        }
    }

    /**
     * Sets the speed to zero, for the speedometer UI dont allow a value of 0
     * for that reason the STOP constant is use.
     */
    public void stop(){
        counter = 0;
        setCarSpeed(counter, "Stopping" );
        speedometer.setSpeed(STOP, DURATION, DELAY);
    }

    /**
     *  Sets the car to move forward, if onReverse is on before moving forward
     *  and car keeps the speed that was before and changes only the direction of the car
     *
     *  sets the direction directionIndirection in the controlPad to show which mode is active.
     *
     *  the same for moveBackward()
     */

    public void moveForward() {
        setUpDirectionIndicator(REVERSE_IS_OFF);
        if(onReverse && counter > 0){
            speedometer.setSpeed(STOP, 800, CHANGE_DIRECTION_DELAY);
            this.onReverse = false;

            setCarSpeed(counter *SPEED, "Reverse speed");

        }else if(onReverse){
            this.onReverse = false;
            if(counter == 0){
                acceleration();
            }
        }else {

        }
        setCarSpeed(counter * SPEED, "Reverse speed");

        driveControl(FORWARD, "Moving forward");
        speedometer.setSpeed(counter * SPEED, DURATION, 100);
    }

    public void moveForwardLeft() {
        driveControl(LEFT, "Moving to the left");
    }

    public void moveForwardRight() {
        driveControl(RIGHT, "Moving right");
    }

    /**
     * See move Forward. The only difference is the direction
     */
    public void moveBackward() {
        setUpDirectionIndicator(REVERSE_IS_ON);

        if(!onReverse && counter > 0){
            speedometer.setSpeed(STOP, 800, CHANGE_DIRECTION_DELAY);
            this.onReverse = true;

        }else {
            this.onReverse = true;
            if(counter == 0){
                acceleration();
            }
        }
        setCarSpeed(counter * -SPEED, "Reverse speed");

        speedometer.setSpeed((counter * SPEED), DURATION, 100);
        driveControl(REVERSE, "Moving in reverse");
    }

    /**
     * Sets the text view in the controlPad to if onReverse is on or off
     * @param direction shows if onReverse is active or not
     */
    public void setUpDirectionIndicator(String direction){
        if(direction.equals(REVERSE_IS_OFF)){
            String red = "#F14C4C";
            directionIndicator.setText(REVERSE_IS_OFF);
            directionIndicator.setTextColor(Color.parseColor(red));
        } else {
            String green  = "#24D510";
            directionIndicator.setText(REVERSE_IS_ON);
            directionIndicator.setTextColor(Color.parseColor(green));
        }
    }

}