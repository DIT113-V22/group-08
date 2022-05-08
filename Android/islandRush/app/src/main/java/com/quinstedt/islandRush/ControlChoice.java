package com.quinstedt.islandRush;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.widget.Button;

public class ControlChoice extends AppCompatActivity {
   Button controlPad, joystick;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_control_choice);

        // Dynamic background
        ConstraintLayout layout = findViewById(R.id.control_choice);
        AnimationDrawable animationBackground = (AnimationDrawable) layout.getBackground();
        animationBackground.setEnterFadeDuration(2500);
        animationBackground.setExitFadeDuration(5000);
        animationBackground.start();

        joystick = findViewById(R.id.button_joystick);
        joystick.setOnClickListener(view -> openJoystick());

        controlPad = findViewById(R.id.button_control);
        controlPad.setOnClickListener(view -> openButtonControl());
    }

    public void openButtonControl() {
        Intent buttonControlIntent = new Intent(this, ControlPad.class);
        startActivity(buttonControlIntent);
    }

    public void openJoystick() {
        Intent joystickIntent = new Intent(this, Joystick.class);
        startActivity(joystickIntent);
    }

}