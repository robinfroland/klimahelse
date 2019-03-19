package com.example.helse


import android.view.View
import android.view.ViewGroup
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import androidx.test.runner.AndroidJUnit4
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers.allOf
import org.hamcrest.TypeSafeMatcher
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class OnboardingActivityTest {

    @Rule
    @JvmField
    var mActivityTestRule = ActivityTestRule(OnboardingActivity::class.java)

    @Test
    fun onboardingActivityTest() {
        val materialButton = onView(
            allOf(
                withId(R.id.setupBtn), withText("Start veiledning"),
                childAtPosition(
                    withParent(withId(R.id.viewPager)),
                    3
                ),
                isDisplayed()
            )
        )
        materialButton.perform(click())

        val materialButton2 = onView(
            allOf(
                withId(R.id.setupBtn), withText("Start veiledning"),
                childAtPosition(
                    withParent(withId(R.id.viewPager)),
                    3
                ),
                isDisplayed()
            )
        )
        materialButton2.perform(click())

        val switch_ = onView(
            allOf(
                withId(R.id.switchMaterial), withText("Bruk min posisjon for varsler"),
                childAtPosition(
                    withParent(withId(R.id.viewPager)),
                    3
                ),
                isDisplayed()
            )
        )
        switch_.perform(click())

        val appCompatTextView = onView(
            allOf(
                withId(R.id.finishBtn), withText("Ferdig"),
                childAtPosition(
                    withParent(withId(R.id.viewPager)),
                    4
                ),
                isDisplayed()
            )
        )
        appCompatTextView.perform(click())
    }

    private fun childAtPosition(
        parentMatcher: Matcher<View>, position: Int
    ): Matcher<View> {

        return object : TypeSafeMatcher<View>() {
            override fun describeTo(description: Description) {
                description.appendText("Child at position $position in parent ")
                parentMatcher.describeTo(description)
            }

            public override fun matchesSafely(view: View): Boolean {
                val parent = view.parent
                return parent is ViewGroup && parentMatcher.matches(parent)
                        && view == parent.getChildAt(position)
            }
        }
    }
}
