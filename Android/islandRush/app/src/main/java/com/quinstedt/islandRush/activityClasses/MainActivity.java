package com.quinstedt.islandRush.activityClasses;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.quinstedt.islandRush.GlobalData;
import com.quinstedt.islandRush.R;
import com.quinstedt.islandRush.Utils;
import com.quinstedt.islandRush.instructionClasses.GettingStarted;

public class MainActivity extends AppCompatActivity {
    private EditText editText;
    private Button openCredits;
    String playerNameInput;
    Animation scaleUp,scaleDown;
    Button enterRace,leaderboard,howToPlayBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editText = findViewById(R.id.playerName);

        //Dynamic background
        ConstraintLayout layout = findViewById(R.id.mainLayout);
        AnimationDrawable animationBackground = (AnimationDrawable) layout.getBackground();
        animationBackground.setEnterFadeDuration(2500);
        animationBackground.setExitFadeDuration(5000);
        animationBackground.start();

        scaleUp= AnimationUtils.loadAnimation(this, R.anim.scale_up);
        scaleDown= AnimationUtils.loadAnimation(this, R.anim.scale_down);

        // On Click goes to Controller choice
        enterRace = findViewById(R.id.button_enterRace);
        enterRace.setOnClickListener(view -> openControlChoice());


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
        playerNameInput = GlobalData.getGlobalData().getPlayerData();
        if(!playerNameInput.isEmpty()){
            editText.setText(playerNameInput);
        }

        openCredits = findViewById(R.id.button_credits);
        openCredits.setOnClickListener(view -> openCredits());

        /**
         * In the MainActivity XML "android:imeOptions="actionSend" changes the Enter Button in the softKeyboard
         * to a Send Button and this method creates a toastMessage after the Send button has been pressed in the keyboard.
         */


        leaderboard = findViewById(R.id.button_Leaderboard);
        leaderboard.setOnClickListener(view -> openLeaderboard());

        howToPlayBtn = findViewById(R.id.howToPlayBtn);
        howToPlayBtn.setOnClickListener(view -> openTutorial());
    }

    /**
     * Opens Instruction screen when the EnterRace button has been pressed
     */
    private void openTutorial() {
        Intent tutorialIntent = new Intent(this, GettingStarted.class);
        howToPlayBtn.startAnimation(scaleUp);
        howToPlayBtn.startAnimation(scaleDown);
        startActivity(tutorialIntent);
    }

    /**
     * Opens Scoreboard when the Leaderboard button has been pressed
     */
    public void openLeaderboard() {
        Intent leadIntent = new Intent(this, Scoreboard.class);
        leaderboard.startAnimation(scaleUp);
        leaderboard.startAnimation(scaleDown);
        startActivity(leadIntent);
    }

    /**
     * Opens ControlChoice when the EnterRace button has been pressed
     */

    public void openCredits() {
        Intent leadIntent = new Intent(this, CreditsActivity.class);
        startActivity(leadIntent);
    }

    public void openControlChoice() {
        enterRace.startAnimation(scaleUp);
        enterRace.startAnimation(scaleDown);
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
