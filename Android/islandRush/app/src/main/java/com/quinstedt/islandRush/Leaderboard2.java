package com.quinstedt.islandRush;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.widget.ImageButton;

public class Leaderboard2 extends AppCompatActivity {
    BrokerConnection connection;
    float x1,x2,y1,y2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaderboard2);

        ImageButton escapeHash = findViewById(R.id.L2Leaderboard_escapeHash);
        escapeHash.setOnClickListener(view -> goBack());

        connection = new BrokerConnection(getApplicationContext());

        connection.setPlayer1(findViewById(R.id.L2Player1));
        connection.setPlayer1(findViewById(R.id.L2Player2));
        connection.setPlayer1(findViewById(R.id.L2Player3));
        connection.setPlayer1(findViewById(R.id.L2Player4));
        connection.setPlayer1(findViewById(R.id.L2Player5));

        connection.setPlayer1(findViewById(R.id.avgSpeed1));
        connection.setPlayer1(findViewById(R.id.avgSpeed2));
        connection.setPlayer1(findViewById(R.id.avgSpeed3));
        connection.setPlayer1(findViewById(R.id.avgSpeed4));
        connection.setPlayer1(findViewById(R.id.avgSpeed5));
        
        connection.connectToMqttBroker();






    }
    public boolean onTouchEvent(MotionEvent touchEvent2){
        switch (touchEvent2.getAction()){
            case MotionEvent.ACTION_DOWN:
                x1 = touchEvent2.getX();
                y1 = touchEvent2.getY();
                break;
            case MotionEvent.ACTION_UP:
                x2 = touchEvent2.getX();
                y2 = touchEvent2.getY();
                if(x1 < x2){
                    Intent leaderboard1 = new Intent(this,Leaderboard1.class);
                    startActivity(leaderboard1);
                }

        }return  false;
    }

    private void goBack() {
        Intent goToMain = new Intent(this, MainActivity.class);
        startActivity(goToMain);
    }
}