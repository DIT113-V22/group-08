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

    Button enterRace, leaderboard;
    EditText editText;

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
       // viewPager2.findViewById(R.id.leaderboard1);

        // On Click goes to Controller choice
        enterRace = findViewById(R.id.button_enterRace);
        enterRace.setOnClickListener(view -> openControlChoice());
        editText = findViewById(R.id.playerName);
        editText.setOnEditorActionListener(editorActionListener);

        leaderboard = findViewById(R.id.button_Leaderboard);
        leaderboard.setOnClickListener(view -> openLeaderboard());
    }

    public void openLeaderboard() {
        Intent leadIntent = new Intent(this, Leaderboard.class);

        startActivity(leadIntent);
    }
    private final TextView.OnEditorActionListener editorActionListener = (textView, actionId, keyEvent) -> {
        if(actionId == EditorInfo.IME_ACTION_SEND){
            String toastMessage = "Saved";
            Toast.makeText(MainActivity.this, toastMessage,Toast.LENGTH_SHORT).show();

        }
        return false;
    };


    public void openControlChoice() {
        Intent raceIntent = new Intent(this, ControlChoice.class);
        startActivity(raceIntent);
    }
}