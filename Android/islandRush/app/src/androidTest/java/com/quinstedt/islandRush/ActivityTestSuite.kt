package com.quinstedt.islandRush

import androidx.test.filters.LargeTest
import org.junit.runner.RunWith
import org.junit.runners.Suite

@RunWith(Suite::class)
@LargeTest
@Suite.SuiteClasses(

    /** Add classes to run after each other */
    MainActivityTest::class,
    ControlChoiceTest::class,
    ControlPadTest::class,
    JoystickTest::class,
    LeaderboardTest::class
)


class ActivityTestSuite {

}

