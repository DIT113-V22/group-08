package com.quinstedt.islandRush;

import static com.quinstedt.islandRush.BrokerConnection.Topics.Connection.QOS;
import static com.quinstedt.islandRush.BrokerConnection.Topics.Race.CONTROLLER_JOYSTICK;
import static com.quinstedt.islandRush.BrokerConnection.Topics.Race.SET_CAR_SPEED;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import android.os.SystemClock;
import android.text.Editable;
import android.text.TextWatcher;

import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Chronometer;
import android.widget.TextView;


import androidx.appcompat.app.AppCompatActivity;

import com.example.joystickjhr.JoystickJhr;

public class Joystick extends AppCompatActivity {

    private int counter = 0;
    private MqttClient mMqttClient;
    private BrokerConnection brokerConnection;
    private SpeedometerView speedometer;
    private final int DURATION = 2000;
    private final int DELAY = 200;
    private final double STOP = 0.0000000001; // use for the speedometer
    private final int SPEED = 10;
    private  int speedMultiplier;
    private Chronometer simpleChronometer;
    private Boolean running = true;
    private Long pauseTime;
    Boolean onReverse = false;
    TextView finish;
    private int currentSpeed;
    private String lastDirection;


    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_joystick);
        speedMultiplier = SPEED;

        finish = findViewById(R.id.finish_joystick);
        brokerConnection = new BrokerConnection(getApplicationContext());
        brokerConnection.setActualSpeed(findViewById(R.id.actualSpeedJoystick));
        brokerConnection.setFinish(finish);
        brokerConnection.setSimpleChronometer(findViewById(R.id.simpleChronometerJoystick));
        mMqttClient = brokerConnection.getMqttClient();
        brokerConnection.connectToMqttBroker();

        // Start
        simpleChronometer = findViewById(R.id.simpleChronometerJoystick); // initiate a chronometer
        simpleChronometer.start(); // start a chronometer


        // Pause and Unpause timer
        Button pause = findViewById(R.id.pauseButtonJoystick); // pause the chronometer
        pause.setOnClickListener(view -> {
            int storedSpeed = currentSpeed;
            if(running) {
                /**
                 * remembers the button has been pressed and the chronometer output
                 */
                simpleChronometer.stop();
                pauseTime = SystemClock.elapsedRealtime() - simpleChronometer.getBase();
                stopCar();
                running = false;
            }
            else {
                simpleChronometer.start();
                simpleChronometer.setBase(SystemClock.elapsedRealtime() - pauseTime);
                currentSpeed = storedSpeed;
                running = true;
            }
        });

        ImageButton escapeHash = findViewById(R.id.joystick_escapeHash);
        escapeHash.setOnClickListener((View view) -> goBack());

        Button brake = findViewById(R.id.brakeJoystick);
        brake.setOnClickListener(view -> brake());

        Button stop = findViewById(R.id.stopJoystick);
        stop.setOnClickListener(view -> stopCar());

        Button acceleration = findViewById(R.id.accelerateJoystick);
        acceleration.setOnClickListener(view -> acceleration());

        Button fullSpeed = findViewById(R.id.fullSpeedJoystick);
        fullSpeed.setOnClickListener(view -> setFullSpeed());

        speedometer = findViewById(R.id.speedometerControlPad);
        speedometer.setLabelConverter((progress, maxProgress) -> String.valueOf((int) Math.round(progress)));

        /** configure value range and ticks in the UI*/
        speedometer.setMaxSpeed(100);
        speedometer.setMajorTickStep(20);
        speedometer.setMinorTicks(0);

        /** Configure value range colors in the UI */
        speedometer.addColoredRange(0, 50, Color.GREEN);
        speedometer.addColoredRange(50, 75, Color.YELLOW);
        speedometer.addColoredRange(75, 100, Color.RED);

        JoystickJhr joystick = findViewById(R.id.joystick);

        joystick.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                joystick.move(motionEvent);

                float angle = getOutputAngle(joystick.angle());
                /**
                 *  When the joystick is release the joystick.angle() has an exact value of 1.0
                 *  and the car is set to continue straight forward.
                 */
                if(angle == 1.0){
                    float zero = 0;
                    driveControl(Float.toString(zero), "Car angle direction");
                }else{
                    driveControl(Float.toString(angle), "Car angle direction");
                }
                setSpeedMultiplier(joystick.angle());


                /**
                 * the speedMultiplier is indicating if the previous speed before the realease of the
                 * joystick was forward (10) or in reverse (-10) and we update the speed to continue
                 * in that direction.
                 */
                if (speedMultiplier == -SPEED) {
                     updateSpeedAfterDirectionChange(true, "Moving in reverse");
                } else {
                    updateSpeedAfterDirectionChange(false, "Moving Forward");
                }
                setupSpeedometer(currentSpeed,DURATION,DELAY);

                return true;
            }
        });

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

    }

    /**
     * Updates the speed sended to the broker
     * @param onReverse a boolean that tells us if the car is moving forward or in reverse
     * @param description the action description that will be printed while sending the information to the broker
     * @return
     */
    public void updateSpeedAfterDirectionChange(boolean onReverse, String description){

        if(!onReverse && counter > 0){
            setupSpeedometer(0,800,1);
            this.onReverse = onReverse; // onReverse changes during the code, so we ignore the warning.
            sendCarSpeed(description);
        }else {
            this.onReverse = onReverse;
            if(counter == 0){
                acceleration();
            }
        }
        changeCurrentSpeed(counter);
        sendCarSpeed(description);

    }
    /**
     * @param description the description that will be printed when the speed is send
     */
    public void sendCarSpeed(String description) {
        String velocityText = "Velocity: " + this.currentSpeed;
        driveSpeed(Integer.toString(this.currentSpeed),description + velocityText);
        String printSpeed = "Speed: ";
        Log.i(printSpeed, velocityText);
    }

    /**
     The methods below are sending the speed to arduino, changing the speedometer UI and printing the velocity
     More details. Look in ControlPad class.
     */
    public void setFullSpeed() {
        counter = SPEED;
        changeCurrentSpeed(counter);
        sendCarSpeed( "Setting velocity on full speed. ");
        setupSpeedometer(currentSpeed,DURATION,DELAY);

    }
    public void brake() {
        if (counter == 1) {
            stopCar();
        } else if (counter > 1){
            counter--;
            changeCurrentSpeed(counter);
            if(onReverse){
                sendCarSpeed("Using Reverse break. ");
            } else{
                sendCarSpeed( "Using forward break. " );
            }
            setupSpeedometer(currentSpeed,DURATION,DELAY);

        }
    }

    public void acceleration() {
        int MAX_COUNTER = 10;
        if (counter < MAX_COUNTER) {
            counter++;
            changeCurrentSpeed(counter);
            if(onReverse){
                sendCarSpeed( "Using Reverse break. ");
            } else{
                sendCarSpeed( "Using forward break. " );
            }
            setupSpeedometer(currentSpeed,DURATION,DELAY);
        }
    }

    public void stopCar (){
        counter = 0;
        changeCurrentSpeed(0);
        sendCarSpeed( "Stopping" );
        setupSpeedometer(0,800,1);
    }


    /**
     * Uses convertAngle to get a value between -180 to 180
     * Where the negative values represent the left directions of the joystick
     * and the positive values represent the right directions.
     * @param joystickAngle the angle of the joystick
     */
    public void setSpeedMultiplier(float joystickAngle){
        if(joystickAngle != 0.0){
            float convertedAngle= convertAngle(joystickAngle);

            if(convertedAngle <= -90 || convertedAngle >= 90 && convertedAngle != 1.0){
                speedMultiplier = -SPEED;
            }else{
                speedMultiplier = SPEED;
            }
        }
    }

    /**
     * Updates the currentSpeed of the car
     *
     * @param counter the speed counter.The counter increases
     *                and decreases depending on the clicks in
     *                the acceleration button and the break button.
     */
    public void changeCurrentSpeed(int counter){
        currentSpeed = counter * speedMultiplier;
    }

    /**
     * @param joystickAngle - The joystick has a angle range between 0 - 360 this method is shifting
     *      * the range.
     * @return  The new range is set to be between -180 and 180.
     */
    public float convertAngle(float joystickAngle){
        return -((joystickAngle + 90) % 360 -180);
    }

    /**
     * Source: https://github.com/DIT113-V22/group-03
     * This method converts the joystick angle and returns a number between 0 and 1.0
     * @param joystickAngle the angle from the joystick
     */
    public float getOutputAngle(float joystickAngle){
        float convertedAngle= convertAngle(joystickAngle);
        int direction;
        if(convertedAngle < 0){
            direction = -1; // to the Left
        }else{
            direction = 1; // to the Right
        }
        float angle;
        // direction set to the opposite angle if reversing
        if(convertedAngle <= -90 || convertedAngle >= 90){
            angle = -(convertedAngle - direction * 180);
        }else{
            angle = convertedAngle;
        }
        return angle/90;
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
        mMqttClient.publish(CONTROLLER_JOYSTICK, message, QOS, null);
        lastDirection = message;
    }
    public void driveSpeed(String message, String actionDescription) {
        brokerConnection.drive(message,actionDescription);
        mMqttClient.publish(SET_CAR_SPEED, message, QOS, null);
    }


    /**
     *
     * @param speed - a positive value representing the speed that the car is in
     * @param duration - animation duration
     * @param delay -  delay after start() has been activated in the animation view
     */
    public void setupSpeedometer(int speed, int duration, int delay) {
        if(speed == 0){
            speedometer.setSpeed(Math.abs(STOP), duration, delay);
        }else if(speed > 0){
            speedometer.setSpeed(speed, duration, delay);
        }else{
            int positive = speed * -1;
            speedometer.setSpeed(positive, duration, delay);
        }

    }
}