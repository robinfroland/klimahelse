package com.example.helse


import android.view.View
import android.view.ViewGroup
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import androidx.test.rule.GrantPermissionRule
import androidx.test.runner.AndroidJUnit4
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers.allOf
import org.hamcrest.TypeSafeMatcher
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@LargeTest
@RunWith(JUnit4::class)
class OnboardingTestEnableAllModules {

    @Rule
    @JvmField
    var mActivityTestRule = ActivityTestRule(MainActivity::class.java)

    @Rule
    @JvmField
    var mGrantPermissionRule =
        GrantPermissionRule.grant(
            "android.permission.ACCESS_FINE_LOCATION"
        )

    @Test
    fun onboardingTestEnableAllModules() {
        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        Thread.sleep(7000)

        val materialButton = onView(
            allOf(
                withId(R.id.setupBtn), withText("KOM I GANG"),
                childAtPosition(
                    withParent(withId(R.id.viewPager)),
                    3
                ),
                isDisplayed()
            )
        )
        materialButton.perform(click())

        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        Thread.sleep(7000)

        val linearLayout = onView(
            allOf(
                childAtPosition(
                    allOf(
                        withId(R.id.recycler_view),
                        childAtPosition(
                            withId(android.R.id.list_container),
                            0
                        )
                    ),
                    1
                ),
                isDisplayed()
            )
        )
        linearLayout.perform(click())

        val linearLayout2 = onView(
            allOf(
                childAtPosition(
                    allOf(
                        withId(R.id.recycler_view),
                        childAtPosition(
                            withId(android.R.id.list_container),
                            0
                        )
                    ),
                    2
                ),
                isDisplayed()
            )
        )
        linearLayout2.perform(click())

        val linearLayout3 = onView(
            allOf(
                childAtPosition(
                    allOf(
                        withId(R.id.recycler_view),
                        childAtPosition(
                            withId(android.R.id.list_container),
                            0
                        )
                    ),
                    3
                ),
                isDisplayed()
            )
        )
        linearLayout3.perform(click())

        val textView = onView(
            allOf(
                withId(R.id.nextBtn), withText("Neste"),
                childAtPosition(
                    childAtPosition(
                        withId(android.R.id.content),
                        0
                    ),
                    3
                ),
                isDisplayed()
            )
        )
        textView.perform(click())

        val linearLayout4 = onView(
            allOf(
                childAtPosition(
                    allOf(
                        withId(R.id.recycler_view),
                        childAtPosition(
                            withId(android.R.id.list_container),
                            0
                        )
                    ),
                    1
                ),
                isDisplayed()
            )
        )
        linearLayout4.perform(click())

        val linearLayout5 = onView(
            allOf(
                childAtPosition(
                    allOf(
                        withId(R.id.recycler_view),
                        childAtPosition(
                            withId(android.R.id.list_container),
                            0
                        )
                    ),
                    2
                ),
                isDisplayed()
            )
        )
        linearLayout5.perform(click())

        val linearLayout6 = onView(
            allOf(
                childAtPosition(
                    allOf(
                        withId(R.id.recycler_view),
                        childAtPosition(
                            withId(android.R.id.list_container),
                            0
                        )
                    ),
                    3
                ),
                isDisplayed()
            )
        )
        linearLayout6.perform(click())

        val textView2 = onView(
            allOf(
                withId(R.id.nextBtn), withText("Neste"),
                childAtPosition(
                    childAtPosition(
                        withId(android.R.id.content),
                        0
                    ),
                    3
                ),
                isDisplayed()
            )
        )
        textView2.perform(click())

        val linearLayout7 = onView(
            allOf(
                childAtPosition(
                    allOf(
                        withId(R.id.recycler_view),
                        childAtPosition(
                            withId(android.R.id.list_container),
                            0
                        )
                    ),
                    1
                ),
                isDisplayed()
            )
        )
        linearLayout7.perform(click())

        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        Thread.sleep(7000)

        val textView3 = onView(
            allOf(
                withId(R.id.finishBtn), withText("Ferdig"),
                childAtPosition(
                    childAtPosition(
                        withId(android.R.id.content),
                        0
                    ),
                    2
                ),
                isDisplayed()
            )
        )
        textView3.perform(click())

        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        Thread.sleep(7000)

        val textView4 = onView(
            allOf(
                withId(R.id.dashboard_title), withText("Kategorier"),
                childAtPosition(
                    allOf(
                        withId(R.id.dashboard_top),
                        childAtPosition(
                            withId(R.id.dashboard_fragment),
                            1
                        )
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        textView4.check(matches(withText("Kategorier")))

        val textView5 = onView(
            allOf(
                withId(R.id.dashboard_description), withText("Trykk for mer informasjon og helsekonsekvenser"),
                childAtPosition(
                    allOf(
                        withId(R.id.dashboard_top),
                        childAtPosition(
                            withId(R.id.dashboard_fragment),
                            1
                        )
                    ),
                    1
                ),
                isDisplayed()
            )
        )
        textView5.check(matches(withText("Trykk for mer informasjon og helsekonsekvenser")))

        val textView6 = onView(
            allOf(
                withId(R.id.module_title), withText("LUFTKVALITET"),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.card_module),
                        0
                    ),
                    1
                ),
                isDisplayed()
            )
        )
        textView6.check(matches(withText("LUFTKVALITET")))

        val textView7 = onView(
            allOf(
                withId(R.id.module_title), withText("UV-STRÅLING"),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.card_module),
                        0
                    ),
                    1
                ),
                isDisplayed()
            )
        )
        textView7.check(matches(withText("UV-STRÅLING")))

        val textView8 = onView(
            allOf(
                withId(R.id.module_title), withText("LUFTFUKTIGHET"),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.card_module),
                        0
                    ),
                    1
                ),
                isDisplayed()
            )
        )
        textView8.check(matches(withText("LUFTFUKTIGHET")))

        val textView9 = onView(
            allOf(
                withId(R.id.largeLabel), withText("Dashbord"),
                childAtPosition(
                    childAtPosition(
                        allOf(withId(R.id.dashboardFragment), withContentDescription("Dashbord")),
                        1
                    ),
                    1
                ),
                isDisplayed()
            )
        )
        textView9.check(matches(withText("Dashbord")))

        val textView10 = onView(
            allOf(
                withId(R.id.smallLabel), withText("Kart"),
                childAtPosition(
                    childAtPosition(
                        allOf(withId(R.id.mapFragment), withContentDescription("Kart")),
                        1
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        textView10.check(matches(withText("Kart")))

        val textView11 = onView(
            allOf(
                withId(R.id.smallLabel), withText("Innstillinger"),
                childAtPosition(
                    childAtPosition(
                        allOf(withId(R.id.settingsFragment), withContentDescription("Innstillinger")),
                        1
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        textView11.check(matches(withText("Innstillinger")))
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

