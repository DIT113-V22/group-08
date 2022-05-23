package com.quinstedt.islandRush;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

public class CreditsActivity extends AppCompatActivity {

    private TextView txt;

    private final String authors = "Safa \n\n David \n\n rami ";

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