package com.quinstedt.islandRush;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

public class Leaderboard2 extends BrokerConnection {
    private Button lead1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaderboard2);

        mMqttClient = new MqttClient(getApplicationContext(), MQTT_SERVER, TAG);
        firstPlayer = findViewById(R.id.firstPlayer);
        secondPlayer = findViewById(R.id.secondPlayer);
        thirdPlayer = findViewById(R.id.thirdPlayer);
        fourPlayer = findViewById(R.id.fourthPlayer);
        fifthPlayer = findViewById(R.id.fifthPlayer);
        speed1 = findViewById(R.id.avgSpeed1);
        speed2 = findViewById(R.id.avgSpeed2);
        speed3 = findViewById(R.id.avgSpeed3);
        speed4 = findViewById(R.id.avgSpeed4);
        speed5 = findViewById(R.id.avgSpeed5);

        connectToMqttBroker();
        //Dynamic background

        // On Click goes to Controller choice
        lead1 = findViewById(R.id.leaderboard_1);
        lead1.setOnClickListener(view -> openLeaderboard1());


    }
        public void openLeaderboard1() {
            Intent leadIntent = new Intent(this, Leaderboard1.class);
            startActivity(leadIntent);
        }

    }
