package com.quinstedt.islandRush

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Espresso.pressBack
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4ClassRunner::class)
class JoystickTest {
    // To run all test classes run ActivityTestSuite

    /** delay between the tests */
    val wait = Thread.sleep(3000)

    @Test
    fun test_isActivityInView(){
        val activityTest = ActivityScenario.launch(Joystick::class.java)
        onView(withId(R.id.joystickActivity)).check(matches(isDisplayed()))
        wait
    }
    @Test
    fun test_isControlPadImageVisible(){
        val activityTest = ActivityScenario.launch(Joystick::class.java)
        onView(withId(R.id.joystick)).check(matches(isDisplayed()))
        wait
    }
    @Test
    fun test_isStopButtonVisible(){
        val activityTest = ActivityScenario.launch(Joystick::class.java)
        onView(withId(R.id.stopJoystick)).check(matches(isDisplayed()))
        wait
    }
    @Test
    fun test_isCarSpeedVisible(){
        val activityTest = ActivityScenario.launch(Joystick::class.java)
        onView(withId(R.id.actualSpeedJoystick)).check(matches(isDisplayed()))
        wait
    }
    @Test
    fun test_GoBackTo_ControlChoice_UsingEscapeHash(){
        val activityTest = ActivityScenario.launch(Joystick::class.java)
        onView(withId(R.id.joystickActivity)).check(matches(isDisplayed()))
        Thread.sleep(500)
        onView(withId(R.id.joystick_escapeHash)).perform(click())
        Thread.sleep(500)
        onView(withId(R.id.controlChoice)).check(matches(isDisplayed()))
        wait
    }

}