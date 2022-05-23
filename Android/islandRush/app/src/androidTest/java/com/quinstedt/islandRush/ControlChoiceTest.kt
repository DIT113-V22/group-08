package com.quinstedt.islandRush

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4ClassRunner::class)
class ControlChoiceTest {
// To run all test classes sequentially  run ActivityTestSuite

    /** delay between the tests */
    private val wait = Utils.delay(3000)

    @Test
    fun test_isActivityInView(){
        val activityTest = ActivityScenario.launch(ControlChoice::class.java)
        onView(withId(R.id.controlChoice)).check(matches(isDisplayed()))
        wait
    }
    @Test
    fun test_visibility_ButtonPad(){
        val activityTest = ActivityScenario.launch(ControlChoice::class.java)
        onView(withId(R.id.button_control)).check(matches(isDisplayed()))
        wait
    }
    @Test
    fun test_visibility_JoystickButton(){
        val activityTest = ActivityScenario.launch(ControlChoice::class.java)
        onView(withId(R.id.button_joystick)).check(matches(isDisplayed()))
        wait
    }
    @Test
    fun test_isTitleTextDisplayed(){
        val activityTest = ActivityScenario.launch(ControlChoice::class.java)
        onView(withId(R.id.titleControlChoice)).check((matches(withText(R.string.control_type))))
        wait
    }
    @Test
    fun test_Navigation_From_ControlChoice_ToJoystickActivity(){
        val activityTest = ActivityScenario.launch(ControlChoice::class.java)
        onView(withId(R.id.button_joystick)).perform(click())
        onView(withId(R.id.joystickActivity)).check(matches(isDisplayed()))
        wait
    }

    @Test
    fun test_Navigation_From_ControlChoice_ToControlPadActivity(){
        val activityTest = ActivityScenario.launch(ControlChoice::class.java)
        onView(withId(R.id.button_control)).perform(click())
        onView(withId(R.id.control_pad)).check(matches(isDisplayed()))
        wait
    }

    @Test
    fun test_GoBackTo_Main_UsingEscapeHash(){
        val activityTest = ActivityScenario.launch(ControlChoice::class.java)
        onView(withId(R.id.controlChoice)).check(matches(isDisplayed()))
        onView(withId(R.id.controlChoice_escapeHash)).perform(click())
        onView(withId(R.id.mainLayout)).check(matches(isDisplayed()))
        wait
    }







}