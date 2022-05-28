package com.quinstedt.islandRush.SplashScreens;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;

import com.quinstedt.islandRush.R;
import com.quinstedt.islandRush.Utils;
import com.quinstedt.islandRush.activityClasses.ControlPad;
import com.quinstedt.islandRush.activityClasses.Joystick;
import com.quinstedt.islandRush.activityClasses.MainActivity;
import com.quinstedt.islandRush.activityClasses.Scoreboard;

public class BeforeRaceStart extends AppCompatActivity {
    Animation scaleUp,scaleDown;
    Button start;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_before_race_start);

        scaleUp= AnimationUtils.loadAnimation(this, R.anim.scale_up);
        scaleDown= AnimationUtils.loadAnimation(this, R.anim.scale_down);

        start = findViewById(R.id.startRace);
        start.setOnClickListener(view -> GotoController());

    }

    private void GotoController() {
        start.startAnimation(scaleUp);
        start.startAnimation(scaleDown);

        if (getIntent().hasExtra("ControlPad")) {
            Intent controlPad = new Intent(this, ControlPad.class);
            controlPad.putExtra("RaceMode", "ControlPad");
            startActivity(controlPad);
        }else{
            Intent joystickIntent = new Intent(this, Joystick.class);
            joystickIntent.putExtra("RaceMode", "Joystick");
            startActivity(joystickIntent);
        }
    }
}