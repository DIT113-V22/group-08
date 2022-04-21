package com.quinstedt.graceapp

import android.content.Intent
import android.graphics.drawable.AnimationDrawable
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout

class ControlChoice : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_control_choice)

        val layout = findViewById<ConstraintLayout>(R.id.control_choice)
        val animationBackground = layout.background as AnimationDrawable
        animationBackground.setEnterFadeDuration(2500)
        animationBackground.setExitFadeDuration(5000)
        animationBackground.start()
        //Joystick = findViewById<Button>(R.id.button_joystick)
       // buttonJoystick.setOnClickListener { view: View? -> openJoystick() }
        val controlButton = findViewById<Button>(R.id.button_control)
        controlButton.setOnClickListener { openButtonControl() }
    }

    private fun openButtonControl() {
        val buttonControlIntent = Intent(this, ControlPad::class.java)
        startActivity(buttonControlIntent)
    }

   /* fun openJoystick() {
        val joystickIntent = Intent(this, Analog_joystick::class.java)
        startActivity(joystickIntent)
    }*/
}