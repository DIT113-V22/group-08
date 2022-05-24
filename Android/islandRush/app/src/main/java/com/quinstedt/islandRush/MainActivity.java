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

public class MainActivity extends AppCompatActivity {
    private EditText editText;
    String playerNameInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        playerNameInput = GlobalData.getGlobalData().getPlayerData();
        //Dynamic background
        ConstraintLayout layout = findViewById(R.id.mainLayout);
        AnimationDrawable animationBackground = (AnimationDrawable) layout.getBackground();
        animationBackground.setEnterFadeDuration(2500);
        animationBackground.setExitFadeDuration(5000);
        animationBackground.start();


        // On Click goes to Controller choice
        Button enterRace = findViewById(R.id.button_enterRace);
        enterRace.setOnClickListener(view -> openControlChoice());

        editText = findViewById(R.id.playerName);
        if(!playerNameInput.isEmpty()){
            editText.setText(playerNameInput);
        }

        /**
         * In the MainActivity XML "android:imeOptions="actionSend" changes the Enter Button in the softKeyboard
         * to a Send Button and this method creates a toastMessage after the Send button has been pressed in the keyboard.
         */

        editText.setOnEditorActionListener((textView, actionId, keyEvent) -> {
            if(actionId == EditorInfo.IME_ACTION_SEND){
                String checkedEmoji = Utils.getEmoji(Utils.CHECKED);
                String toastMessage = "Saved " + checkedEmoji;
                Toast.makeText(MainActivity.this, toastMessage,Toast.LENGTH_LONG).show();
                playerNameInput = editText.getText().toString().trim();
                GlobalData.getGlobalData().setPlayerData(playerNameInput);
            }
            return false;
        });

        Button leaderboard = findViewById(R.id.button_Leaderboard);
        leaderboard.setOnClickListener(view -> openLeaderboard());

    }

    public void openLeaderboard() {
        Intent leadIntent = new Intent(this, Scoreboard.class);
        startActivity(leadIntent);
    }

    /**
     * Opens ControlChoice when the EnterRace button has been pressed
     */

    public void openControlChoice() {
        if(playerNameInput.isEmpty()){
            String happyEmoji = Utils.getEmoji(Utils.HAPPY);
            String toastMessage = "Enter a name " +happyEmoji;
            Toast.makeText(MainActivity.this, toastMessage,Toast.LENGTH_LONG).show();
        }else{
            Intent raceIntent = new Intent(this, ControlChoice.class);
            startActivity(raceIntent);
        }

    }

}
