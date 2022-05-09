package com.quinstedt.islandRush

import android.os.IBinder

import android.view.WindowManager
import androidx.test.espresso.Root
import org.hamcrest.Description
import org.hamcrest.TypeSafeMatcher

/**
 * Author: QA automated
 * Website: http://www.qaautomated.com/2016/01/how-to-test-toast-message-using-espresso.html
 */
class ToastMatcher : TypeSafeMatcher<Root?>() {
    /**
     * It is also possible to use the BoundedMatcher, which does some extra checking "behind the scenes"
     * But we have choice to use the TypeSageMatcher, which is a simpler test.
     */

    /** Use for debugging */
    override fun describeTo(description: Description?) {
        description?.appendText("is toast")
    }

    /**
     * Checks if the root type is a Toast and if the toast is in the window that is visible.
     *
     * @param root -> root view in the app
     * @return true if the root of type Toast and if it attached to a window in the app
     *
     * for more information about getWindowToken() and getApplicationWindowToken()
     * see:
     * https://developer.android.com/reference/android/view/View#getWindowToken()
     * https://developer.android.com/reference/android/view/View#getApplicationWindowToken()
     *
     */
    override fun matchesSafely(item: Root?): Boolean {
        val type: Int? = item?.getWindowLayoutParams()?.get()?.type
        if (type == WindowManager.LayoutParams.TYPE_TOAST) {
            val windowToken: IBinder = item.getDecorView().getWindowToken()
            val appToken: IBinder = item.getDecorView().getApplicationWindowToken()
            if (windowToken === appToken) { /** means this window isn't contained by any other windows. */
                return true
            }
        }
        return false
    }

}