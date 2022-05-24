package com.quinstedt.islandRush.activityClasses

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*


import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.quinstedt.islandRush.OrientationChangeAction
import com.quinstedt.islandRush.R
import com.quinstedt.islandRush.Utils
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4ClassRunner::class)
class ControlPadTest {
// To run all test classes sequentially  run ActivityTestSuite

    /** delay between the tests */
    val wait = Utils.delay(3000)

    @Test
    fun test_isActivityInView(){
        val activityTest = ActivityScenario.launch(ControlPad::class.java)
        onView(withId(R.id.control_pad)).check(matches(isDisplayed()))
        wait
    }
    /**
     * The most common reason for this test to fail is the use of different ID between
     * the portrait mode and the landscape mode. This makes the app to crash while changing orientation.
     *
     * Reminder: The ID are case sensitive
     *
     * Also, it is a good practice to make sure to treat the ID as you treat your attributes.
     * Make sure they are self-explanatory and if different layouts have the same type of components make sure
     * that you can differentiate them.
     */

    @Test
    fun test_landscapeMode(){
        val activityTest = ActivityScenario.launch(ControlPad::class.java)
        onView(isRoot()).perform(OrientationChangeAction.orientationLandscape());
        Utils.delay(3000)
        onView(withId(R.id.control_pad)).check(matches(isDisplayed()))
    }
    @Test
    fun test_isControlPadImageVisible(){
        val activityTest = ActivityScenario.launch(ControlPad::class.java)
        onView(withId(R.id.controlBackground)).check(matches(isDisplayed()))
        wait
    }
    @Test
    fun test_isCarSpeedVisible(){
        val activityTest = ActivityScenario.launch(ControlPad::class.java)
        onView(withId(R.id.actualSpeed)).check(matches(isDisplayed()))
        wait
    }
    @Test
    fun test_GoBackTo_ControlChoice_UsingEscapeHash(){
        val activityTest = ActivityScenario.launch(ControlPad::class.java)
        onView(withId(R.id.control_pad)).check(matches(isDisplayed()))
        onView(withId(R.id.controlPad_escapeHash)).perform(click())
        onView(withId(R.id.controlChoice)).check(matches(isDisplayed()))
        wait
    }
    @Test
    fun test_Buttons_Visibility(){
        val activityTest = ActivityScenario.launch(ControlPad::class.java)
        onView(withId(R.id.stopControlPad)).check(matches(isDisplayed()))
        onView(withId(R.id.brakeControlPad)).check(matches(isDisplayed()))
        onView(withId(R.id.accelerateControlPad)).check(matches(isDisplayed()))
        onView(withId(R.id.fullSpeedControlPad)).check(matches(isDisplayed()))
        onView(withId(R.id.resetButtonControlPad)).check(matches(isDisplayed()))
        onView(withId(R.id.pauseButtonControlPad)).check(matches(isDisplayed()))
        wait
    }



}