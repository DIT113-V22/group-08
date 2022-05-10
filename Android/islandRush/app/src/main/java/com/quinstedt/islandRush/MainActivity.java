package com.quinstedt.islandRush;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MainActivity extends MovementConnection {

    private Button button, leaderboard1;
    EditText player;
    String playerName;
    final static String nameTopic =  "IslandRush/Server/name";
    //ViewPager2 viewPager2;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Dynamic background
        RelativeLayout layout = findViewById(R.id.mainLayout);
        AnimationDrawable animationBackground = (AnimationDrawable) layout.getBackground();
        animationBackground.setEnterFadeDuration(2500);
        animationBackground.setExitFadeDuration(5000);
        animationBackground.start();

        EditText player= findViewById(R.id.playerName);
        playerName = player.getText().toString();
       // viewPager2.findViewById(R.id.leaderboard1);
        mMqttClient = new MqttClient(getApplicationContext(), MQTT_SERVER, TAG);

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
        mMqttClient.publish(nameTopic,playerName,1,null);
        Intent raceIntent = new Intent(this, ControlChoice.class);
        startActivity(raceIntent);
    }
}