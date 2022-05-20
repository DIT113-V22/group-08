package com.quinstedt.islandRush;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.quinstedt.islandRush.database.PlayerScore;
import com.quinstedt.islandRush.database.ViewModal;

public class SaveScore extends AppCompatActivity {

    EditText editTextPersonName;
    EditText editTextTime;
    Button saveBtn;
    private ViewModal viewmodal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save_score);
        editTextPersonName = findViewById(R.id.editTextPersonName);
        editTextTime = findViewById(R.id.editTextTime);
        viewmodal = ViewModelProviders.of(this).get(ViewModal.class);
        saveBtn = findViewById(R.id.saveBtn);
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                insertDataToDatabase();
            }
        });
    }

    private void goBackToHome() {
        Intent goBackToHome = new Intent(this, MainActivity.class);
        startActivity(goBackToHome);
    }

    private void insertDataToDatabase() {
        String playerName = editTextPersonName.getText().toString();
        String text = editTextTime.getText().toString();
        Integer time= Integer.parseInt(text);

        if(checkInput(playerName,time)){
            // Create PlayerScore Object
            PlayerScore playerScore = new PlayerScore(playerName,time);
            // Add Data to Database
            viewmodal.insert(playerScore);
            Toast.makeText(this, "Successfully saved!", Toast.LENGTH_LONG).show();
            goBackToHome();
        }else if(!checkInput(playerName,time)){
            Toast.makeText(this, "Please fill out all fields.", Toast.LENGTH_LONG).show();
        }else {
            Toast.makeText(this, "Score not saved", Toast.LENGTH_SHORT).show();
        }
    }
    private Boolean checkInput(String name, int time){
        return !(name.isEmpty());
    }
}