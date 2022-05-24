package com.quinstedt.islandRush.activityClasses;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.quinstedt.islandRush.GlobalData;
import com.quinstedt.islandRush.R;
import com.quinstedt.islandRush.adapters.PlayerScoreRVAdapter;
import com.quinstedt.islandRush.database.PlayerScore;
import com.quinstedt.islandRush.database.ViewModal;

import java.util.List;

public class Scoreboard extends AppCompatActivity {


    private RecyclerView RV;
    private ViewModal viewmodal;
    private ImageButton resetBtn,backToHome;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scoreboard);

        // initializing controls on the activity
        resetBtn= findViewById(R.id.resetBtn);
        resetBtn.setOnClickListener(view -> deleteLeaderboard());
        backToHome=findViewById(R.id.homeBtn);
        backToHome.setOnClickListener(view -> goToHome());
        RV = findViewById(R.id.idRvScoreboard);

        // setting layout manager to our adapter class.
        RV.setLayoutManager(new LinearLayoutManager(this));
        RV.setHasFixedSize(true);

        // initializing adapter for recycler view.
        final PlayerScoreRVAdapter adapter = new PlayerScoreRVAdapter();

        // setting adapter class for recycler view.
        RV.setAdapter(adapter);

        // passing a data from view modal.
        viewmodal = ViewModelProviders.of(this).get(ViewModal.class);

        // below line is use to get all the player scores from view modal.
        viewmodal.getAllScores().observe(this, new Observer<List<PlayerScore>>() {
            @Override
            public void onChanged(List<PlayerScore> playerScores) {
                // when the data is changed in our PlayerScore object we are
                // adding that list to our adapter class.
                adapter.submitList(playerScores);
            }
        });

    }

    private void goToHome() {
        Intent goToMainIntent=new Intent(this, MainActivity.class);
        startActivity(goToMainIntent);
    }

    private void deleteLeaderboard() {
        viewmodal.deleteAllScores();

    }
}
