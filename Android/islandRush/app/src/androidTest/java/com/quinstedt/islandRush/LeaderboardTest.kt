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

    val wait = Thread.sleep(3000)
    /** NAVIGATION TESTS*/
    @Test
    fun test_Launching_LeaderboardActivity(){
        val activityTest = ActivityScenario.launch(Leaderboard::class.java)
        onView(withId(R.id.leaderboardMain)).check(matches(isDisplayed()))
        onView(withId(R.id.leaderboard1)).check(matches(isDisplayed()))
        wait
    }

    @Test
    fun test_SwipeToLeaderboard2(){
        val activityTest = ActivityScenario.launch(Leaderboard::class.java)
        onView(withId(R.id.leaderboardMain)).perform(swipeLeft())
        onView(withId(R.id.leaderboard2)).check(matches(isDisplayed()))
        wait

    }
    @Test
    fun test_SwipeToLeaderboard1(){
        val activityTest = ActivityScenario.launch(Leaderboard::class.java)
        onView(withId(R.id.leaderboardMain)).perform(swipeRight())
        onView(withId(R.id.leaderboard1)).check(matches(isDisplayed()))
        wait
    }
    @Test
    fun test_clickTab_ToLeaderboard2(){
        val activityTest = ActivityScenario.launch(Leaderboard::class.java)
        onView(withText("Leaderboard 2")).perform(click());
        onView(withId(R.id.leaderboard2)).check(matches(isDisplayed()))
        wait
    }
    @Test
    fun test_clickTab_ToLeaderboard1(){
        val activityTest = ActivityScenario.launch(Leaderboard::class.java)
        onView(withId(R.id.leaderboardMain)).perform(swipeLeft())
        onView(withText("Leaderboard 1")).perform(click());
        onView(withId(R.id.leaderboard1)).check(matches(isDisplayed()))
        wait
    }

    /** LAYOUT TESTS*/ //  L2
    @Test
    fun test_leaderboard1_Ranking(){
        lateinit var scenario: FragmentScenario<Leaderboard1>
        scenario = launchFragmentInContainer(themeResId = R.style.Theme_IslandRush)
        scenario.moveToState(Lifecycle.State.STARTED)

        onView(withId(R.id.rank1)).check(matches(isDisplayed()))
        onView(withId(R.id.rank2)).check(matches(isDisplayed()))
        onView(withId(R.id.rank3)).check(matches(isDisplayed()))
        onView(withId(R.id.rank4)).check(matches(isDisplayed()))
        onView(withId(R.id.rank5)).check(matches(isDisplayed()))
        wait
    }
    @Test
    fun test_leaderboard2_Ranking(){
        lateinit var scenario: FragmentScenario<Leaderboard2>
        scenario = launchFragmentInContainer(themeResId = R.style.Theme_IslandRush)
        scenario.moveToState(Lifecycle.State.STARTED)
        onView(withId(R.id.L2rank1)).check(matches(isDisplayed()))
        onView(withId(R.id.L2rank2)).check(matches(isDisplayed()))
        onView(withId(R.id.L2rank3)).check(matches(isDisplayed()))
        onView(withId(R.id.L2rank4)).check(matches(isDisplayed()))
        onView(withId(R.id.L2rank5)).check(matches(isDisplayed()))
        wait
    }
    @Test
    fun test_leaderBoard1_MedalsImages(){
        lateinit var scenario: FragmentScenario<Leaderboard1>
        scenario = launchFragmentInContainer(themeResId = R.style.Theme_IslandRush)
        scenario.moveToState(Lifecycle.State.STARTED)
        onView(withId(R.id.gold_medal)).check(matches(isDisplayed()))
        onView(withId(R.id.silver_medal)).check(matches(isDisplayed()))
        onView(withId(R.id.bronze_medal)).check(matches(isDisplayed()))
        wait
    }
    @Test
    fun test_leaderBoard2_MedalsImages(){
        lateinit var scenario: FragmentScenario<Leaderboard2>
        scenario = launchFragmentInContainer(themeResId = R.style.Theme_IslandRush)
        scenario.moveToState(Lifecycle.State.STARTED)
        onView(withId(R.id.L2gold_medal)).check(matches(isDisplayed()))
        onView(withId(R.id.L2silver_medal)).check(matches(isDisplayed()))
        onView(withId(R.id.L2bronze_medal)).check(matches(isDisplayed()))
        wait
    }
    @Test
    fun test_escapeHash(){
        val activityTest = ActivityScenario.launch(Leaderboard::class.java)
        onView(withId(R.id.leaderboard_escapeHash)).perform(click());
        onView(withId(R.id.mainLayout)).check(matches(isDisplayed()))
    }


}