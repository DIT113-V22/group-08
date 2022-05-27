package com.quinstedt.islandRush.activityClasses;

import static com.quinstedt.islandRush.BrokerConnection.Topics.Connection.QOS;
import static com.quinstedt.islandRush.BrokerConnection.Topics.Race.CONTROLLER_CONTROLPAD;
import static com.quinstedt.islandRush.BrokerConnection.Topics.Race.SET_CAR_SPEED;


import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Chronometer;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.quinstedt.islandRush.BrokerConnection;
import com.quinstedt.islandRush.MqttClient;
import com.quinstedt.islandRush.R;
import com.quinstedt.islandRush.SpeedometerView;
import com.quinstedt.islandRush.SplashScreens.LeaderboardAnimation;

public class ControlPad extends AppCompatActivity {

    private BrokerConnection brokerConnection;
    private SpeedometerView speedometer;
    private int counter;
    private Boolean onReverse = false;
    private Boolean running = true;
    private int currentSpeed;
    private Long pauseTime;
    TextView directionIndicator;
    TextView finish;

    private final int DURATION = 2000;
    private final int DELAY = 500;
    private final int CHANGE_DIRECTION_DELAY = 100;
    private final String REVERSE_IS_ON = "REVERSE ON";
    private final String REVERSE_IS_OFF = "REVERSE OFF";

    private final int MAX_COUNTER = 10;

    private final String FORWARD = "1";
    private final String RIGHT = "2";
    private final String REVERSE = "3";
    private final String LEFT = "4";

    @Override
    public void onCreate(Bundle savedInstanceState ) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_control_pad);
        counter = 1;
        finish = findViewById(R.id.finish_controlPad);
        brokerConnection = new BrokerConnection(getApplicationContext());
        brokerConnection.setActualSpeed(findViewById(R.id.actualSpeed));
        brokerConnection.setFinish(finish);
        brokerConnection.setSimpleChronometer(findViewById(R.id.simpleChronometerControlPad));
        brokerConnection.connectToMqttBroker();

        /** Start timer */
        Chronometer simpleChronometer = findViewById(R.id.simpleChronometerControlPad);
        simpleChronometer.start();


        /** Pause and Unpause timer */
        Button pause = findViewById(R.id.pauseButtonControlPad); // pause the chronometer
        pause.setOnClickListener(view -> {
            if(running) {
                /**
                 * remembers the button has been pressed and the chronometer output
                 */
                simpleChronometer.stop();
                pauseTime = SystemClock.elapsedRealtime() - simpleChronometer.getBase();
                stopCar();
                String red = "#F14C4C";
                pause.setTextColor(Color.parseColor(red));
                running = false;
            } else {
                simpleChronometer.start();
                simpleChronometer.setBase(SystemClock.elapsedRealtime() - pauseTime);
                String red = "#FFFFFFFF";
                pause.setTextColor(Color.parseColor(red));
                running = true;
            }
        });

        Button reset = findViewById(R.id.resetButtonControlPad);
        reset.setOnClickListener(view -> {
            simpleChronometer.setBase(SystemClock.elapsedRealtime());
            this.currentSpeed = 0;
            this.counter = 0;
            stopCar();
            running = true;
            onReverse = false;
            driveControl("1", "Resume game.");
            simpleChronometer.start();
        });


        ImageButton escapeHash = findViewById(R.id.controlPad_escapeHash);
        escapeHash.setOnClickListener(view -> goBack());

        Button forward = findViewById(R.id.up);
        forward.setOnClickListener(view -> moveForward());

        Button reverse = findViewById(R.id.down);
        reverse.setOnClickListener(view -> moveInReverse());

        Button left = findViewById(R.id.left);
        /**
         * ACTION_DOWN is for when the button is press, the car moves to the left
         * ACTION_UP is for when the button is realese making the car continue forward
         * and it stops rotating to the left.
         *
         * same as for the right button below.
         */
        left.setOnTouchListener((view, event) -> {
            if(running) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    moveLeft();
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (onReverse) {
                        sendMqttControlMessage(REVERSE, "Moving forward");
                        sendCarSpeed("Continue backwards");
                    } else {
                        sendMqttControlMessage(FORWARD, "Moving forward");
                        sendCarSpeed("Continue  forward");
                    }
                    setupSpeedometer(currentSpeed, DURATION, 100);
                }
            }
            return true;
        });

        Button right = findViewById(R.id.right);

        /**
         * see the comment above for the left button.
         */
        right.setOnTouchListener((view, event) -> {
            if(running) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    moveRight();
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (onReverse) {
                        sendMqttControlMessage(REVERSE, "Moving forward");
                        sendCarSpeed("Continue backwards");
                        setupSpeedometer(currentSpeed, DURATION, DELAY);
                    } else {
                        sendMqttControlMessage(FORWARD, "Moving forward");
                        sendCarSpeed("Continue  forward");
                        speedometer.setSpeed(currentSpeed, DURATION, DELAY);
                    }
                }
            }
            return true;
        });

        directionIndicator = findViewById(R.id.direction_indicator);


        Button brake = findViewById(R.id.brakeControlPad);
        brake.setOnClickListener(view -> brake());

        Button stop = findViewById(R.id.stopControlPad);
        stop.setOnClickListener(view -> stopCar());

        Button acceleration = findViewById(R.id.accelerateControlPad);
        acceleration.setOnClickListener(view -> acceleration());

        Button fullSpeed = findViewById(R.id.fullSpeedControlPad);
        fullSpeed.setOnClickListener(view -> setFullSpeed());

        Intent animationScore = new Intent(this, LeaderboardAnimation.class);
        finish.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(finish.getText().toString().equalsIgnoreCase("FINISH")){
                    try {
                        Thread.sleep(3000);
                        startActivity(animationScore);
                    }catch (Exception exception){
                        exception.getStackTrace();
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

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

    }
    /**
     * Sets the car to full speed, depending on if in reverse is active to make sure
     * that if we are on reverse the cars can continue in that direction.
     * Also sets the speedometer UI to the max value
     */
    public void setFullSpeed() {
        if(running) {
            counter = MAX_COUNTER;
            changeCurrentSpeed(counter);
            sendCarSpeed("Setting velocity on full speed. ");
            setupSpeedometer(currentSpeed, DURATION, DELAY);
        }
    }

    /**
     * This method decreases the speed of the car taking into account if onReverse is on or off
     * and sets the speedometer UI to the new speed value
     */
    public void brake() {
        if(running) {
            if (counter == 1) {
                stopCar();
            } else if (counter > 1) {
                counter--;
                changeCurrentSpeed(counter);
                sendCarSpeed("Using Reverse break. ");
                setupSpeedometer(currentSpeed, DURATION, DELAY);
            }
        }
    }

    /**
     * This method increases the speed of the car and sets the speedometor UI to the new value
     * also taking into account the if onReverse is on or off.
     */
    public void acceleration() {
        if(running) {
            if (counter == 0 && !onReverse) {
                setUpDirectionIndicator(REVERSE_IS_OFF);
            }

            if (counter < MAX_COUNTER) {
                counter++;
                changeCurrentSpeed(counter);
                sendCarSpeed("Using reverse brea.");
                setupSpeedometer(currentSpeed, DURATION, DELAY);
            }
        }
    }

    /**
     * Sets the speed to zero, for the speedometer UI dont allow a value of 0
     * for that reason the STOP constant is use.
     */
    public void stopCar(){
        if(running) {
            counter = 0;
            changeCurrentSpeed(counter);
            sendCarSpeed("Stopping");
            setupSpeedometer(0, DURATION, DELAY);
        }
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
        if(running) {
            setUpDirectionIndicator(REVERSE_IS_OFF);
            if (onReverse && counter > 0) {
                setupSpeedometer(0, 800, CHANGE_DIRECTION_DELAY);
                this.onReverse = false;

                sendCarSpeed("Reverse speed");

            } else if (onReverse) {
                this.onReverse = false;
                if (counter == 0) {
                    acceleration();
                }
            }
            changeCurrentSpeed(counter);
            sendCarSpeed("Reverse speed");
            sendMqttControlMessage(FORWARD, "Moving forward");
            setupSpeedometer(currentSpeed, DURATION, 100);
        }
    }

    public void moveLeft() {
        if(running) {
            sendMqttControlMessage(LEFT, "Moving to the left");
        }
    }

    public void moveRight() {
        if(running) {
            sendMqttControlMessage(RIGHT, "Moving right");
        }
    }

    /**
     * See move Forward. The only difference is the direction
     */
    public void moveInReverse() {
        if(running) {
            setUpDirectionIndicator(REVERSE_IS_ON);

            if (!onReverse && counter > 0) {
                setupSpeedometer(0, 800, CHANGE_DIRECTION_DELAY);
                this.onReverse = true;

            } else {
                this.onReverse = true;
                if (counter == 0) {
                    acceleration();
                }
            }
            changeCurrentSpeed(counter);
            sendCarSpeed("Reverse speed");
            setupSpeedometer(currentSpeed, DURATION, 100);
            sendMqttControlMessage(REVERSE, "Moving in reverse");
        }
    }

    public void changeCurrentSpeed(int counter) {
        int SPEED = 10;
        if(onReverse){
            this.currentSpeed = counter * ( -1 * SPEED);
        }else{
            this.currentSpeed = counter * SPEED;
        }
    }

    /**
     * Launches the ControlChoice after that the escape Hash has been clicked
     */
    private void goBack() {
        stopCar();
        Intent controlChoiceActivity = new Intent(this, ControlChoice.class);
        startActivity(controlChoiceActivity);
    }

    /** One drive method for the movement of the car and one for the speed
     * See BrokerConnection.
     * @param message - the message that we send to the broker
     * @param actionDescription - the action description that will be printed
     */

    public void sendMqttControlMessage(String message, String actionDescription) {
        brokerConnection.publishMqttMessage(message,actionDescription);
        brokerConnection.mqttClient.publish(CONTROLLER_CONTROLPAD, message,QOS, null);
    }

    public void sendMqttSpeedMessage(String message, String actionDescription) {
        brokerConnection.publishMqttMessage(message,actionDescription);
        brokerConnection.mqttClient.publish(SET_CAR_SPEED, message,QOS, null);
    }


    public void sendCarSpeed(String description ){

        String velocityText = "Velocity: " + currentSpeed;
        sendMqttSpeedMessage(Integer.toString(currentSpeed),description + velocityText);
        String printSpeed = "Speed: ";
        Log.i(printSpeed, velocityText);

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
    public void setupSpeedometer(int speed, int duration, int delay) {
        if(speed == 0){
            // must be a number close to zero, because the speedometer doest allow the value 0
            double STOP = 0.0000000001;
            speedometer.setSpeed(Math.abs(STOP), duration, delay);
        }else if(speed > 0){
            speedometer.setSpeed(speed, duration, delay);
        }else{
            int positive = speed * -1;
            speedometer.setSpeed(positive, duration, delay);
        }

    }

}