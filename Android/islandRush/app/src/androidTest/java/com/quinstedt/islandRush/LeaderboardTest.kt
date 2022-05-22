package com.quinstedt.islandRush

import androidx.fragment.app.testing.FragmentScenario
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.lifecycle.Lifecycle
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4

import org.junit.Test
import org.junit.runner.RunWith



@RunWith(AndroidJUnit4::class)
class LeaderboardTest {
// To run all test classes sequentially  run ActivityTestSuite

    /*
    val wait = Thread.sleep(3000)
    /** NAVIGATION TESTS*/
    @Test
    fun test_Launching_LeaderboardActivity(){
        val activityTest = ActivityScenario.launch(Scoreboard::class.java)
        onView(withId(R.id.scoreboard)).check(matches(isDisplayed()))
        onView(withId(R.id.scoreboard)).check(matches(isDisplayed()))
        wait
    }

    @Test
    fun test_SwipeToLeaderboard2(){
        val activityTest = ActivityScenario.launch(Leaderboard1::class.java)
        onView(withId(R.id.Leaderboard1)).perform(swipeLeft())
        onView(withId(R.id.Leaderboard2)).check(matches(isDisplayed()))
        wait

    }
    @Test
    fun test_SwipeToLeaderboard1(){
        val activityTest = ActivityScenario.launch(Leaderboard2::class.java)
        onView(withId(R.id.Leaderboard2)).perform(swipeRight())
        onView(withId(R.id.Leaderboard1)).check(matches(isDisplayed()))
        wait
    }
    @Test
    fun test_clickTab_ToLeaderboard2(){
        val activityTest = ActivityScenario.launch(Leaderboard1::class.java)
        onView(withId(R.id.GoToLeaderboard2)).perform(click());
        onView(withId(R.id.Leaderboard2)).check(matches(isDisplayed()))
        wait
    }
    @Test
    fun test_clickTab_ToLeaderboard1(){
        val activityTest = ActivityScenario.launch(Leaderboard2::class.java)
        onView(withId(R.id.GoToLeaderboard1)).perform(click());
        onView(withId(R.id.Leaderboard1)).check(matches(isDisplayed()))
        wait
    }

    /** LAYOUT TESTS*/ //  L2
    @Test
    fun test_leaderboard1_Ranking(){
        val activityTest = ActivityScenario.launch(Leaderboard1::class.java)

        onView(withId(R.id.Rank1)).check(matches(isDisplayed()))
        onView(withId(R.id.Rank2)).check(matches(isDisplayed()))
        onView(withId(R.id.Rank3)).check(matches(isDisplayed()))
        onView(withId(R.id.Rank4)).check(matches(isDisplayed()))
        onView(withId(R.id.Rank5)).check(matches(isDisplayed()))
        wait
    }
    @Test
    fun test_leaderboard2_Ranking(){
        val activityTest = ActivityScenario.launch(Leaderboard2::class.java)

        onView(withId(R.id.L2Rank1)).check(matches(isDisplayed()))
        onView(withId(R.id.L2Rank2)).check(matches(isDisplayed()))
        onView(withId(R.id.L2Rank3)).check(matches(isDisplayed()))
        onView(withId(R.id.L2Rank4)).check(matches(isDisplayed()))
        onView(withId(R.id.L2Rank5)).check(matches(isDisplayed()))
        wait
    }
    @Test
    fun test_leaderBoard1_MedalsImages(){
        val activityTest = ActivityScenario.launch(Leaderboard1::class.java)
        onView(withId(R.id.Gold_medal)).check(matches(isDisplayed()))
        onView(withId(R.id.Silver_medal)).check(matches(isDisplayed()))
        onView(withId(R.id.Bronze_medal)).check(matches(isDisplayed()))
        wait
    }
    @Test
    fun test_leaderBoard2_MedalsImages(){
        val activityTest = ActivityScenario.launch(Leaderboard1::class.java)
        onView(withId(R.id.L2Gold_medal)).check(matches(isDisplayed()))
        onView(withId(R.id.L2Silver_medal)).check(matches(isDisplayed()))
        onView(withId(R.id.L2Bronze_medal)).check(matches(isDisplayed()))
        wait
    }
    @Test
    fun test_escapeHash_Leaderboard1(){
        val activityTest = ActivityScenario.launch(Leaderboard1::class.java)
        onView(withId(R.id.Leaderboard_escapeHash)).perform(click());
        onView(withId(R.id.mainLayout)).check(matches(isDisplayed()))
    }
    @Test
    fun test_escapeHash_Leaaderboard2(){
        val activityTest = ActivityScenario.launch(Leaderboard2::class.java)
        onView(withId(R.id.L2Leaderboard_escapeHash)).perform(click())
        onView(withId(R.id.mainLayout)).check(matches(isDisplayed()))
    }

    **/
}