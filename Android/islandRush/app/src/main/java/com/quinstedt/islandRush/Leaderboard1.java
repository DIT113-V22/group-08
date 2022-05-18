package com.quinstedt.islandRush;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.widget.ImageButton;



public class Leaderboard1 extends AppCompatActivity {


     BrokerConnection connection;
     float x1,x2,y1,y2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaderboard1);

        ImageButton escapeHash = findViewById(R.id.Leaderboard_escapeHash);
        escapeHash.setOnClickListener(view -> goBack());
        connection = new BrokerConnection(getApplicationContext());
        connection.setPlayer1(findViewById(R.id.Player1));
        connection.setPlayer1(findViewById(R.id.Player2));
        connection.setPlayer1(findViewById(R.id.Player3));
        connection.setPlayer1(findViewById(R.id.Player4));
        connection.setPlayer1(findViewById(R.id.Player5));

        connection.setPlayer1(findViewById(R.id.Time1));
        connection.setPlayer1(findViewById(R.id.Time2));
        connection.setPlayer1(findViewById(R.id.Time3));
        connection.setPlayer1(findViewById(R.id.Time4));
        connection.setPlayer1(findViewById(R.id.Time5));
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
                if(x1 > x2){
                    Intent leaderboard2 = new Intent(this,Leaderboard2.class);
                    startActivity(leaderboard2);
                }

        }return  false;
    }

    private void goBack() {
        Intent goToMain = new Intent(this, MainActivity.class);
        startActivity(goToMain);
    }



}