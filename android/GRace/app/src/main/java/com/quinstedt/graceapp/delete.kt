package com.quinstedt.graceapp

import android.content.Intent
import android.graphics.drawable.AnimationDrawable
import android.os.Bundle
import android.view.View
import android.widget.RelativeLayout
import androidx.appcompat.app.AppCompatActivity

class delete : AppCompatActivity() {
    Button enterRace
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.delete)

        //background
        val layout = findViewById<RelativeLayout>(R.id.mainLayout)
        val animationBackground = layout.background as AnimationDrawable
        animationBackground.setEnterFadeDuration(2500)
        animationBackground.setExitFadeDuration(5000)
        animationBackground.start()

        // race_layout on click
        button = findViewById(R.id.button_enterRace)
        button!!.setOnClickListener(View.OnClickListener { view: View? -> openControlChoice() })
    }

    fun openControlChoice() {
        val raceIntent = Intent(this, ControlChoice::class.java)
        startActivity(raceIntent)
    }
}