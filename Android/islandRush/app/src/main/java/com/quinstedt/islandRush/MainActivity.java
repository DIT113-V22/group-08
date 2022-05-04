package com.quinstedt.islandRush;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.widget.Button;
import android.widget.RelativeLayout;

public class MainActivity extends AppCompatActivity {

    private Button button, leaderboard1;
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

        // On Click goes to Controller choice
        button = findViewById(R.id.button_enterRace);
        button.setOnClickListener(view -> openControlChoice());

        leaderboard1 = findViewById(R.id.button_Leaderboard);
        leaderboard1.setOnClickListener(view -> openLeaderboard1());
    }
    public void openLeaderboard1() {
            Intent leaderboard1Intent = new Intent(this, Leaderboard1.class);
            startActivity(leaderboard1Intent);
        }
        public void openControlChoice() {
        Intent raceIntent = new Intent(this, Control_choice.class);
        startActivity(raceIntent);
    }
}