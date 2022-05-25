package com.quinstedt.islandRush.instructionClasses;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.widget.ImageButton;
import com.quinstedt.islandRush.R;
import com.quinstedt.islandRush.activityClasses.MainActivity;

public class gettingStarted extends AppCompatActivity {
    ImageButton exitBtn;
    float x1,x2,y1,y2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_getting_started);
        exitBtn= findViewById(R.id.exitBtn);
        exitBtn.setOnClickListener(view -> goBack());

    }

    /**
     * This method  is used to swipe to the next screen
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
                if(x1 > x2){
                    Intent nextScreen = new Intent(this,GameModes.class);
                    startActivity(nextScreen);
                }
        }return  false;
    }

    private void goBack() {
        Intent exitTutorial = new Intent(this, MainActivity.class);
        startActivity(exitTutorial);
    }



}