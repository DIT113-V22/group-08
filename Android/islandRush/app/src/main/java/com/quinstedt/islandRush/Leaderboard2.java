package com.quinstedt.islandRush;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

public class Leaderboard2 extends AppCompatActivity {
    private Button lead1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaderboard2);
        //Dynamic background

        // On Click goes to Controller choice
        lead1 = findViewById(R.id.leaderboard_1);
        lead1.setOnClickListener(view -> openLeaderboard1());
    }
        public void openLeaderboard1() {
            Intent leadIntent = new Intent(this, Leaderboard1.class);
            startActivity(leadIntent);
        }

    }
