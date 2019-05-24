package com.example.helse


import android.view.View
import android.view.ViewGroup
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import androidx.test.runner.AndroidJUnit4
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers.allOf
import org.hamcrest.TypeSafeMatcher
import org.hamcrest.core.IsInstanceOf
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class TestEmptyDashboardTextShowsAndAddingModulesFromSettingsWork {

    @Rule
    @JvmField
    var mActivityTestRule = ActivityTestRule(MainActivity::class.java)

    @Test
    fun testEmptyDashboardTextShowsAndAddingModulesFromSettingsWork() {
        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        Thread.sleep(7000)

        val textView = onView(
            allOf(
                withId(R.id.onboardingDescription),
                withText("Klimahelse gir deg informasjon om hvordan været negativt kan påvirke din helse. Følg denne korte veiledningen for skreddersy appen for dine behov!"),
                childAtPosition(
                    withParent(withId(R.id.viewPager)),
                    2
                ),
                isDisplayed()
            )
        )
        textView.check(matches(withText("Klimahelse gir deg informasjon om hvordan været negativt kan påvirke din helse. Følg denne korte veiledningen for skreddersy appen for dine behov!")))

        val textView2 = onView(
            allOf(
                withId(R.id.onboardingTitle), withText("Velkommen til klimahelse!"),
                childAtPosition(
                    withParent(withId(R.id.viewPager)),
                    1
                ),
                isDisplayed()
            )
        )
        textView2.check(matches(withText("Velkommen til klimahelse!")))

        val imageView = onView(
            allOf(
                withId(R.id.imageView), withContentDescription("Logo"),
                childAtPosition(
                    withParent(withId(R.id.viewPager)),
                    0
                ),
                isDisplayed()
            )
        )
        imageView.check(matches(isDisplayed()))

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

        val textView3 = onView(
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
        textView3.perform(click())

        val textView4 = onView(
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
        textView4.perform(click())

        val textView5 = onView(
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
        textView5.perform(click())

        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        Thread.sleep(7000)

        val textView6 = onView(
            allOf(
                withId(R.id.empty_dashboard_title), withText("Dashbordet er tomt"),
                childAtPosition(
                    allOf(
                        withId(R.id.empty_dashboard_text),
                        childAtPosition(
                            withId(R.id.dashboard_fragment),
                            2
                        )
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        textView6.check(matches(withText("Dashbordet er tomt")))

        val textView7 = onView(
            allOf(
                withId(R.id.empty_dashboard_desc), withText("Gå til innstillinger for å aktivere kategorier"),
                childAtPosition(
                    allOf(
                        withId(R.id.empty_dashboard_text),
                        childAtPosition(
                            withId(R.id.dashboard_fragment),
                            2
                        )
                    ),
                    1
                ),
                isDisplayed()
            )
        )
        textView7.check(matches(withText("Gå til innstillinger for å aktivere kategorier")))

        val textView8 = onView(
            allOf(
                withId(R.id.search_dashboard), withText("Alnabru, Oslo"),
                childAtPosition(
                    allOf(
                        withId(R.id.dashboard_fragment),
                        childAtPosition(
                            withId(R.id.nav_host_fragment),
                            0
                        )
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        textView8.check(matches(withText("Alnabru, Oslo")))

        val bottomNavigationItemView = onView(
            allOf(
                withId(R.id.settingsFragment), withContentDescription("Innstillinger"),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.bottom_navbar),
                        0
                    ),
                    2
                ),
                isDisplayed()
            )
        )
        bottomNavigationItemView.perform(click())

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
                    6
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
                    7
                ),
                isDisplayed()
            )
        )
        linearLayout2.perform(click())

        val bottomNavigationItemView2 = onView(
            allOf(
                withId(R.id.dashboardFragment), withContentDescription("Dashbord"),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.bottom_navbar),
                        0
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        bottomNavigationItemView2.perform(click())

        val textView12 = onView(
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
        textView12.check(matches(withText("LUFTKVALITET")))

        val textView13 = onView(
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
        textView13.check(matches(withText("UV-STRÅLING")))

        val textView14 = onView(
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
        textView14.check(matches(withText("Kategorier")))

        val textView15 = onView(
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
        textView15.check(matches(withText("Trykk for mer informasjon og helsekonsekvenser")))
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
