package com.quinstedt.islandRush;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Switch;

public class ControlChoice extends AppCompatActivity {

    Button controlPad, joystick;
    ImageButton escapeHash;
    boolean cameraOn = true;
    boolean speedometerOn = true;
  //  Switch camera;
  //  Switch speedometer;
  
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_control_choice);
      
        ConstraintLayout layout = findViewById(R.id.controlChoice);
        AnimationDrawable animationBackground = (AnimationDrawable) layout.getBackground();
        animationBackground.setEnterFadeDuration(2500);
        animationBackground.setExitFadeDuration(5000);
        animationBackground.start();

        escapeHash =  findViewById(R.id.controlChoice_escapeHash);
        escapeHash.setOnClickListener(view -> goBack());
        joystick = findViewById(R.id.button_joystick);
        joystick.setOnClickListener(view -> openJoystick());

        joystick = findViewById(R.id.joystickNoCamera);
        joystick.setOnClickListener(view -> openJoystickNoCamera());

        controlPad = findViewById(R.id.button_control);
        controlPad.setOnClickListener(view -> openButtonControl());

    }

    private void openJoystickNoCamera() {
        Intent joystick2Intent = new Intent(this, Joystick2.class);
        startActivity(joystick2Intent);
    }
     /*
       Testing Switch option for the controlChoice

       cameraSwitch = findViewById(R.id.switchControlPad);


        cameraSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if(isChecked){

                }else{

                }
            }
        });

       */

    /**
     * Method for the escape Hash that launches MainActivity
     */
    private void goBack() {
        Intent goToMain = new Intent(this, MainActivity.class);
        startActivity(goToMain);
    }

    /**
     * Launches ControlPad when ButtonPad button is click
     */
    public void openButtonControl() {
        Intent buttonControlIntent = new Intent(this, ControlPad.class);
        startActivity(buttonControlIntent);
    }
    /**
     * Launches Joystick Activity when joystick button is click
     */
    public void openJoystick() {
        Intent joystickIntent = new Intent(this, Joystick.class);
        startActivity(joystickIntent);
    }

}