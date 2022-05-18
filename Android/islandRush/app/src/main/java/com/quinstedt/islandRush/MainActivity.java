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
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    Button enterRace, leaderboard;
    EditText editText;
    BrokerConnection brokerConnection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Dynamic background
        ConstraintLayout layout = findViewById(R.id.mainLayout);
        AnimationDrawable animationBackground = (AnimationDrawable) layout.getBackground();
        animationBackground.setEnterFadeDuration(2500);
        animationBackground.setExitFadeDuration(5000);
        animationBackground.start();


        // On Click goes to Controller choice
        enterRace = findViewById(R.id.button_enterRace);
        enterRace.setOnClickListener(view -> openControlChoice());
        editText = findViewById(R.id.playerName);
        editText.setOnEditorActionListener(editorActionListener);

        leaderboard = findViewById(R.id.button_Leaderboard);
        leaderboard.setOnClickListener(view -> openLeaderboard());

        brokerConnection = new BrokerConnection(getApplicationContext());
        brokerConnection.connectToMqttBroker();

    }

    public void openLeaderboard() {
        Intent leadIntent = new Intent(this, Leaderboard1.class);

        startActivity(leadIntent);

    }

    /**
     * In the MainActivity XML "android:imeOptions="actionSend" changes the Enter Button in the softKeyboard
     * to a Send Button and this method creates a toastMessage after the Send button has been pressed in the keyboard.
     */
    private TextView.OnEditorActionListener editorActionListener = new TextView.OnEditorActionListener() {
        @Override
        public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
            if(actionId == EditorInfo.IME_ACTION_SEND){
                String toastMessage = "Saved";
                Toast.makeText(MainActivity.this, toastMessage,Toast.LENGTH_LONG).show();

            }
           return false;
        }
    };

    /**
     * Opens ControlChoice when the EnterRace button has been pressed
     */

    public void openControlChoice() {
        Intent raceIntent = new Intent(this, ControlChoice.class);
        startActivity(raceIntent);
    }
}
