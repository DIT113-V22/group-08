package com.quinstedt.islandRush;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends BrokerConnection {

    Button enterRace, leaderboard;
    EditText editText;
    String playerName;
    final static String nameTopic =  "IslandRush/Server/name";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Dynamic background
        ConstraintLayout layout = findViewById(R.id.mainLayout);
        AnimationDrawable animationBackground = (AnimationDrawable) layout.getBackground();
        animationBackground.setEnterFadeDuration(2500);
        animationBackground.setExitFadeDuration(5000);
        animationBackground.start();

        mMqttClient = new MqttClient(getApplicationContext(), MQTT_SERVER, TAG);

        // On Click goes to Controller choice
        enterRace = findViewById(R.id.button_enterRace);
        enterRace.setOnClickListener(view -> openControlChoice());

        editText = findViewById(R.id.playerName);
        editText.setOnEditorActionListener(editorActionListener);

        leaderboard = findViewById(R.id.button_Leaderboard);
        leaderboard.setOnClickListener(view -> openLeaderboard());

        connectToMqttBroker();
    }
    private final TextView.OnEditorActionListener editorActionListener = (textView, actionId, keyEvent) -> {
        if(actionId == EditorInfo.IME_ACTION_SEND){
            String toastMessage = "Saved";
            Toast.makeText(MainActivity.this, toastMessage,Toast.LENGTH_SHORT).show();

        }
        return false;
    };

    public void openLeaderboard() {
        Intent leadIntent = new Intent(this, Leaderboard1.class);

        startActivity(leadIntent);
    }

    public void openControlChoice() {
        playerName= editText.getText().toString();
        if(!playerName.isEmpty()){
            mMqttClient.publish(nameTopic,playerName,1,null);
            Intent raceIntent = new Intent(this, ControlChoice.class);
            startActivity(raceIntent);
        }else{
            String message = "Player name can't be empty.";
            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
        }
    }
}