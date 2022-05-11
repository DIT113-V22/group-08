package com.quinstedt.islandRush;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

public class Leaderboard1 extends AppCompatActivity {
    private Button lead2,goBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaderboard1);


        lead2 = findViewById(R.id.leaderboard_2);
        lead2.setOnClickListener(view -> openLeaderboard2());

        goBack = findViewById(R.id.go_back);
        goBack.setOnClickListener(view -> openMainActivity());

    }

    public void openLeaderboard2() {
        Intent lead2Intent = new Intent(this, Leaderboard2.class);
        startActivity(lead2Intent);

}

    public void openMainActivity() {
        Intent lead2Intent = new Intent(this, MainActivity.class);
        startActivity(lead2Intent);
    }



}