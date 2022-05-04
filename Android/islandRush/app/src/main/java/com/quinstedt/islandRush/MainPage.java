package com.quinstedt.islandRush;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.widget.Button;
import android.widget.RelativeLayout;

public class MainPage extends AppCompatActivity {
    Button button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);
        //Dynamic background
        RelativeLayout layout = findViewById(R.id.mainPage);
        AnimationDrawable animationBackground = (AnimationDrawable) layout.getBackground();
        animationBackground.setEnterFadeDuration(2500);
        animationBackground.setExitFadeDuration(5000);
        animationBackground.start();

        // On Click goes to Controller choice
        button = findViewById(R.id.button_enterRace);
        button.setOnClickListener(view -> openControlChoice());


    }

    public void openControlChoice() {
        Intent raceIntent = new Intent(this, Control_choice.class);
        startActivity(raceIntent);
    }
}