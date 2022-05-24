package com.quinstedt.islandRush.activityClasses

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.filters.LargeTest
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.quinstedt.islandRush.R
import com.quinstedt.islandRush.ToastMatcher
import com.quinstedt.islandRush.Utils
import com.quinstedt.islandRush.OrientationChangeAction.Companion.orientationLandscape
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4ClassRunner::class)
class MainActivityTest {
// To run all test classes sequentially  run ActivityTestSuite

    /** delay between the tests */
    val wait = Utils.delay(4500)

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
        val activityTest = ActivityScenario.launch(MainActivity::class.java)
        onView(isRoot()).perform(orientationLandscape());
        Utils.delay(3000)
        onView(withId(R.id.mainLayout)).check(matches(isDisplayed()))
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
    fun test_Navigation_From_MainActivity_To_ControlChoice(){
        val activityTest = ActivityScenario.launch(MainActivity::class.java)
        onView(withId(R.id.playerName)).perform(click())
        val playerName = "PlayerTest1"
        onView(withId(R.id.playerName)).perform(typeText(playerName))
        onView(withId(R.id.playerName)).perform(pressImeActionButton())
        onView(withId(R.id.button_enterRace)).perform(click())
        onView(withId(R.id.controlChoice)).check(matches(isDisplayed()))
        wait
    }

    @Test
    fun test_Navigation_From_MainActivity_To_LeaderBoard(){
        val activityTest = ActivityScenario.launch(MainActivity::class.java)
        onView(withId(R.id.button_Leaderboard)).perform(click())
        onView(withId(R.id.scoreboard)).check(matches(isDisplayed()))
    }

    /** @LargeTest - the test is ignore and dont run
     *  @Test- runs the test
     *  To run this test uncomment @Test
     *  NOTE: to avoid problems with the CI make sure to comment @Test */


   @LargeTest
  //  @Test
    fun test_process_of_entering_a_playerName(){
        val activityTest = ActivityScenario.launch(MainActivity::class.java)
        onView(withId(R.id.playerName)).perform(click())
        val playerName = "PlayerTest1"
        onView(withId(R.id.playerName)).perform(typeText(playerName))
        onView(withId(R.id.playerName)).perform(pressImeActionButton())
        Utils.delay(2000)
        onView(withText(playerName)).check(matches(isDisplayed()))
        val checkedEmoji = Utils.getEmoji(Utils.CHECKED)
        val toastMessage = "Saved $checkedEmoji"
        onView(withText(toastMessage)).inRoot(ToastMatcher()).check(matches(isDisplayed()))
        wait
    }
}