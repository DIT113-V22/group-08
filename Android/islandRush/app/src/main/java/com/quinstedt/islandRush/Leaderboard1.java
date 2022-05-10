package com.quinstedt.islandRush;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

public class Leaderboard1 extends MovementConnection {
    private Button lead2,goBack;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaderboard1);

        mMqttClient = new MqttClient(getApplicationContext(), MQTT_SERVER, TAG);
        player1 = (TextView) findViewById(R.id.player1);
        player2 = findViewById(R.id.player2);
        player3 = findViewById(R.id.player3);
        player4 = findViewById(R.id.player4);
        player5 = findViewById(R.id.player5);
        firstTime = findViewById(R.id.time1);
        secondTime = findViewById(R.id.time2);
        thirdTime = findViewById(R.id.time3);
        fourthTime = findViewById(R.id.time4);
        fifthTime = findViewById(R.id.time5);

        connectToMqttBroker();

        lead2 = findViewById(R.id.leaderboard_2);
        lead2.setOnClickListener(view -> openLeaderboard2());

        goBack = findViewById(R.id.go_back);
        goBack.setOnClickListener(view -> openMainActivity());


    }

    public void openLeaderboard2() {
        Intent lead2Intent = new Intent(this, Leaderboard2.class);
        startActivity(lead2Intent);

}

    public void openMainActivity() {
        Intent lead2Intent = new Intent(this, MainActivity.class);
        startActivity(lead2Intent);
    }



}