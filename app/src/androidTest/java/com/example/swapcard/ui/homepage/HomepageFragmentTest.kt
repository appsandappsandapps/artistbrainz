package com.example.swapcard.ui.homepage

import androidx.test.espresso.Espresso.*
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.espresso.assertion.ViewAssertions.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.platform.app.InstrumentationRegistry
import com.example.swapcard.MainActivity
import com.example.swapcard.R
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Running against dummy data -- doesn't touch the internet
 *
 * Values based on the ArtistsRepositoryInMemory
 */
@RunWith(AndroidJUnit4::class)
@LargeTest
class HomepageFragmentTest {

  @get:Rule
  val rule = ActivityScenarioRule(MainActivity::class.java)

  @Test fun checkViewPagerDisplayed() {
    onView(withId(R.id.homepage_viewpager2))
      .check(matches(isDisplayed()))
  }

  @Test fun checkViewPagerTabsDisplayed() {
    onView(withId(R.id.homepage_tab_layout))
      .check(matches(isDisplayed()))
  }

}