package com.quinstedt.islandRush;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.widget.Button;
import android.widget.RelativeLayout;

public class MainActivity extends AppCompatActivity {

    private Button button, leaderboard1;
    //ViewPager2 viewPager2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Dynamic background
        RelativeLayout layout = findViewById(R.id.mainLayout);
        AnimationDrawable animationBackground = (AnimationDrawable) layout.getBackground();
        animationBackground.setEnterFadeDuration(2500);
        animationBackground.setExitFadeDuration(5000);
        animationBackground.start();
       // viewPager2.findViewById(R.id.leaderboard1);

        // On Click goes to Controller choice
        button = findViewById(R.id.button_enterRace);
        button.setOnClickListener(view -> openControlChoice());

        leaderboard1 = findViewById(R.id.button_Leaderboard);
        leaderboard1.setOnClickListener(view -> openLeaderboard1());
    }

    public void openLeaderboard1() {
        Intent leadIntent = new Intent(this, Leaderboard1.class);

        startActivity(leadIntent);
    }


    public void openControlChoice() {
        Intent raceIntent = new Intent(this, ControlChoice.class);
        startActivity(raceIntent);
    }
}