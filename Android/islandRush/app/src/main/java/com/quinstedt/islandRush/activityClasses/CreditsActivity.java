package com.quinstedt.islandRush.activityClasses;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.quinstedt.islandRush.R;

public class CreditsActivity extends AppCompatActivity {

    private TextView txt;

    private final String authors = "Credits: \n\n Safa Youssef \n\n Sergey Balan \n\n Andreea Lavinia Fulger \n\n  Nicole Andrea Quinstedt \n\n Faisal Sayed \n\n Danesh Mohammadi \n\n";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_credits);
        Animation animation= AnimationUtils.loadAnimation(getApplicationContext(),R.anim.credits);



        txt =(TextView) findViewById(R.id.txtView);
        txt.setText(authors);

        txt.startAnimation(animation);
    }
}