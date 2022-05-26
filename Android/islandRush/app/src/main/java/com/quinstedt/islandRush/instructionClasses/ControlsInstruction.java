package com.quinstedt.islandRush.instructionClasses;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.widget.ImageButton;

import com.quinstedt.islandRush.R;
import com.quinstedt.islandRush.activityClasses.MainActivity;

public class ControlsInstruction extends AppCompatActivity {

    ImageButton exitBtn3;
    float x1,x2,y1,y2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_controls_instruction);

        exitBtn3= findViewById(R.id.exitBtn3);
        exitBtn3.setOnClickListener(view -> goBack());
    }

    private void goBack() {
        Intent exitTutorial = new Intent(this, MainActivity.class);
        startActivity(exitTutorial);
    }

    /**
     * This method  is used to swipe to the previous or the next screen
     */
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
                    Intent previousScreen = new Intent(this,GameModes.class);
                    startActivity(previousScreen);
                }else if(x1>x2){
                    Intent nextScreen = new Intent(this, CheckLeaderboard.class);
                    startActivity(nextScreen);}

        }return  false;
    }
}