package com.quinstedt.islandRush

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*


import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4ClassRunner::class)
class ControlPadTest {
// To run all test classes sequentially  run ActivityTestSuite

    /** delay between the tests */
    val wait =Utils.delay(3000)

    @Test
    fun test_isActivityInView(){
        val activityTest = ActivityScenario.launch(ControlPad::class.java)
        onView(withId(R.id.control_pad)).check(matches(isDisplayed()))
        wait
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
    fun test_speedButton_inView(){
        val activityTest = ActivityScenario.launch(ControlPad::class.java)
    }



}