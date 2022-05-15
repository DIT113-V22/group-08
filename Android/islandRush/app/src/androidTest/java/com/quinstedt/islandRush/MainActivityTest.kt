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
class MainActivityTest {
// To run all test classes run ActivityTestSuite

    /** delay between the tests */
    val wait = Thread.sleep(4000)

    /**
     * Not using a global activityTest:
     * @get: Rule
     * val activityTestRule: ActivityScenarioRule<MainActivity> = ActivityScenarioRule(MainActivity::class.java)
     *
     * While using global activityTest not all test are passing when they are run together
     * but they are passing individually. To avoid this problem the activity is launch in every test.
     * That means that the activityTest is isolated to the function and is possible to test
     * specific properties that we cannot achive by using the global activityTest.
     */

    @Test
    fun test_isActivityInView(){
        val activityTest = ActivityScenario.launch(MainActivity::class.java)
        onView(withId(R.id.mainLayout)).check(matches(isDisplayed()))
        wait

    }
    /**
     * To the visibility of a text :
     * onView(withId(R.id.<id name of the text>)check(matches(isDisplayed()))
     *
     * Another way to test visibility in case isDisplay is not working
     *
     * change isDisplayed() to withEffectiveVisibility(Visibility.VISIBLE)
     * the are different types but with the same functionality
     */
    @Test
    fun test_visibility_OfTheLogo(){
        val activityTest = ActivityScenario.launch(MainActivity::class.java)
        onView(withId(R.id.main_logo)).check(matches(isDisplayed()))
    }
    @Test
    fun test_visibility_RaceButton(){
        val activityTest = ActivityScenario.launch(MainActivity::class.java)
        onView(withId(R.id.button_enterRace)).check(matches(isDisplayed()))
        wait
    }
    @Test
    fun test_visibility_LeaderBoardButton(){
        val activityTest = ActivityScenario.launch(MainActivity::class.java)
        onView(withId(R.id.button_Leaderboard)).check(matches(isDisplayed()))
        wait
    }
    @Test
    fun test_visibility_And_Text_for_EnterPlayerName(){
        val activityTest = ActivityScenario.launch(MainActivity::class.java)
        onView(withId(R.id.playerName)).check(matches(withHint(R.string.Enter_players_name)))
        wait
    }
    @Test
    fun test_process_of_entering_a_playerName(){
        val activityTest = ActivityScenario.launch(MainActivity::class.java)
        onView(withId(R.id.playerName)).perform(click())
        val playerName = "PlayerTest1"
        onView(withId(R.id.playerName)).perform(typeText(playerName))
        onView(withId(R.id.playerName)).perform(pressImeActionButton())
        onView(withText(playerName)).check(matches(isDisplayed()))
        Thread.sleep(1000)
        val toastMessage = "Saved"
        onView(withText(toastMessage)).inRoot(ToastMatcher()).check(matches(isDisplayed()))
        wait
    }
    @Test
    fun test_Navigation_From_MainActivity_To_ControlChoice(){
        val activityTest = ActivityScenario.launch(MainActivity::class.java)
        onView(withId(R.id.button_enterRace)).perform(click())
        onView(withId(R.id.controlChoice)).check(matches(isDisplayed()))
        wait
    }

/** Not passing because the leaderboard has not yet been implemented */

    @Test
    fun test_Navigation_From_MainActivity_To_LeaderBoard(){
        val activityTest = ActivityScenario.launch(MainActivity::class.java)
        onView(withId(R.id.button_Leaderboard)).perform(click())
        onView(withId(R.id.leaderboardMain)).check(matches(isDisplayed()))
    }








}