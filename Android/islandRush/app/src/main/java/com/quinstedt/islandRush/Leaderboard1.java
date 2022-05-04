package com.quinstedt.islandRush;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

public class Leaderboard1 extends AppCompatActivity {
    private Button leaderboard2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaderboard1);
        leaderboard2 = findViewById(R.id.leaderboard2);
        leaderboard2.setOnClickListener(view -> openLeaderboard1());
    }
    public void openLeaderboard1() {
        Intent leaderboard2Intent = new Intent(this, Leaderboard2.class);
        startActivity(leaderboard2Intent);
    }
}