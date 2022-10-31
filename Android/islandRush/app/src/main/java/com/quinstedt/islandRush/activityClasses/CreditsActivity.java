package com.quinstedt.islandRush.activityClasses;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.TextView;

import com.quinstedt.islandRush.R;

public class CreditsActivity extends AppCompatActivity {

    private TextView txt;
    Animation scaleUp,scaleDown;
    ImageButton escapeHash;
    private final String authors = "Credits: \n\n Safa Youssef \n\n Sergey Balan \n\n Andreea Lavinia Fulger \n\n  Nicole Andrea Quinstedt \n\n Faisal Sayed \n\n Danesh Mohammadi \n\n";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_credits);
        Animation animation= AnimationUtils.loadAnimation(getApplicationContext(),R.anim.credits);

        scaleUp= AnimationUtils.loadAnimation(this, R.anim.scale_up);
        scaleDown= AnimationUtils.loadAnimation(this, R.anim.scale_down);

        txt =(TextView) findViewById(R.id.txtView);
        txt.setText(authors);

        txt.startAnimation(animation);

        escapeHash = findViewById(R.id.credits_escapeHash);
        escapeHash.setOnClickListener(view -> goBack());

    }

    private void goBack() {
        escapeHash.startAnimation(scaleUp);
        escapeHash.startAnimation(scaleDown);
        Intent goToMain = new Intent(this, MainActivity.class);
        startActivity(goToMain);
    }
}
