package com.quinstedt.islandRush.activityClasses

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.quinstedt.islandRush.OrientationChangeAction
import com.quinstedt.islandRush.R
import com.quinstedt.islandRush.Utils

import org.junit.Test
import org.junit.runner.RunWith



@RunWith(AndroidJUnit4::class)
class ScoreboardTest {
// To run all test classes sequentially  run ActivityTestSuite

    val wait = Utils.delay(3000)

    @Test
    fun test_isScoreboardInView(){
        val activityTest = ActivityScenario.launch(Scoreboard::class.java)
        onView(withId(R.id.scoreboard)).check(matches(isDisplayed()))
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
        val activityTest = ActivityScenario.launch(Scoreboard::class.java)
        onView(isRoot()).perform(OrientationChangeAction.orientationLandscape());
        Utils.delay(3000)
        onView(withId(R.id.scoreboard)).check(matches(isDisplayed()))
    }
    @Test
    fun Test_areTheRanking_visible(){
        val activityTest = ActivityScenario.launch(Scoreboard::class.java)
        onView(withId(R.id.medal1)).check(matches(isDisplayed()))
        onView(withId(R.id.medal2)).check(matches(isDisplayed()))
        onView(withId(R.id.medal3)).check(matches(isDisplayed()))
        onView(withId(R.id.number4)).check(matches(isDisplayed()))
        onView(withId(R.id.number5)).check(matches(isDisplayed()))
    }

    @Test
    fun Test_areTheTitles_visible(){
        val activityTest = ActivityScenario.launch(Scoreboard::class.java)
        onView(withId(R.id.scoreboardTitle)).check(matches(isDisplayed()))
        onView(withId(R.id.nameTitle)).check(matches(isDisplayed()))
        onView(withId(R.id.timeTile)).check(matches(isDisplayed()))
    }
    @Test
    fun Test_escapeHash(){
        val activityTest = ActivityScenario.launch(Scoreboard::class.java)
        onView(withId(R.id.homeBtn)).check(matches(isDisplayed()))
        onView(withId(R.id.homeBtn)).perform(click())
        onView(withId(R.id.mainLayout)).check(matches(isDisplayed()))
    }









}