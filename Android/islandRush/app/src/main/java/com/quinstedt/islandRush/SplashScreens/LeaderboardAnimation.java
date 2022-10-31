package com.quinstedt.islandRush.SplashScreens;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.quinstedt.islandRush.GlobalData;
import com.quinstedt.islandRush.R;
import com.quinstedt.islandRush.activityClasses.Scoreboard;
import com.quinstedt.islandRush.Utils;
import com.quinstedt.islandRush.database.PlayerScore;
import com.quinstedt.islandRush.database.ViewModal;

public class LeaderboardAnimation extends AppCompatActivity {

    TextView raceMessage, playerNameAnim, timeAnim, finnishMessageAnim;
    ImageView carImageAnim, mainImageAnim;
    private ViewModal viewmodal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaderboard_animation);

        // initialize view model to act as an interface between the database and UI
        viewmodal = ViewModelProviders.of(this).get(ViewModal.class);

        raceMessage =  findViewById(R.id.raceMessage_anim);
        playerNameAnim = findViewById(R.id.player_name_anim);
        timeAnim = findViewById(R.id.timing_anim);
        mainImageAnim = findViewById(R.id.main_page_anim);
        carImageAnim = findViewById(R.id.car_image_anim);
        finnishMessageAnim =findViewById(R.id.message_end_anim);
        mainImageAnim = findViewById(R.id.main_page_anim);

        playerNameAnim.setText(GlobalData.getGlobalData().getPlayerData());
        String applause = Utils.getEmoji(Utils.APPLAUSE);
        String raceMessage = "Good job! " + applause;
        this.raceMessage.setText(raceMessage);
        timeAnim.setText(GlobalData.getGlobalData().getRaceTime());
        String crossedFingers = Utils.getEmoji(Utils.CROSSED_FINGERS);
        String finishMessage = "Good luck next time " + crossedFingers;
        finnishMessageAnim.setText(finishMessage);

        insertDataToDatabase();

        Animation slideBackground= AnimationUtils.loadAnimation(getApplicationContext(),R.anim.slide_background);
        Animation textSlideOut=AnimationUtils.loadAnimation(getApplicationContext(),R.anim.slide_out_text);
        Animation moveCar= AnimationUtils.loadAnimation(getApplicationContext(),R.anim.car_move);
        Animation textSlideIn= AnimationUtils.loadAnimation(getApplicationContext(),R.anim.slide_in_text);

        carImageAnim.setAnimation(moveCar);


        //slide in the texts
        playerNameAnim.setAnimation(textSlideIn);
        timeAnim.setAnimation(textSlideIn);
        this.raceMessage.setAnimation(textSlideIn);
        finnishMessageAnim.setAnimation(textSlideIn);

        //rotate the texts

        playerNameAnim.animate().rotationBy(720).setDuration(2000).setStartDelay(2000);
        timeAnim.animate().rotationBy(720).setDuration(2000).setStartDelay(2000);
        this.raceMessage.animate().rotationBy(720).setDuration(2000).setStartDelay(2000);

        //Slide out the texts
        playerNameAnim.setAnimation(textSlideOut);
        timeAnim.setAnimation(textSlideOut);
        this.raceMessage.setAnimation(textSlideOut);
        finnishMessageAnim.setAnimation(textSlideOut);
        mainImageAnim.setAnimation(slideBackground);

        slideBackground.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                finish();
                startActivity(new Intent(getApplicationContext(), Scoreboard.class));
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });


    }

    // Inserts the player Name and Time taken to the database
    private void insertDataToDatabase() {
        String playerName= GlobalData.getGlobalData().getPlayerData();
        int time= GlobalData.getGlobalData().getTimeInSec();
        // Create PlayerScore Object
        PlayerScore playerScore = new PlayerScore(playerName,time);
        // Add Data to Database
        viewmodal.insert(playerScore);

    }
}