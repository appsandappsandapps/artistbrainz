package com.example.swapcard.ui.search

import androidx.test.espresso.Espresso.*
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.espresso.assertion.ViewAssertions.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.example.swapcard.MainActivity
import com.example.swapcard.R
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import androidx.test.espresso.IdlingRegistry

import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ActivityScenario.ActivityAction

import org.junit.Before

import androidx.test.espresso.IdlingResource

/**
 * Running against dummy data -- doesn't touch the internet
 *
 * Values based on the ArtistsRepositoryInMemory
 */
@RunWith(AndroidJUnit4::class)
@LargeTest
class SearchListFragmentTest {

  @get:Rule val rule = ActivityScenarioRule(MainActivity::class.java)

  @Test fun checkSearchResults() {
    onView(withId(R.id.edittext)).perform(
      typeText("anything"),
      pressImeActionButton()
    )
    onView(withText("Nina Simone")).check(matches(isDisplayed()))
  }

  @Test fun checkClearTextInput() {
    onView(withId(R.id.edittext)).perform(
      typeText("text input"),
      pressImeActionButton()
    )
    onView(withText("text input")).check(matches(isDisplayed()))
    onView(withId(R.id.search_clearText)).perform(click())
    onView(withText("text input")).check(doesNotExist())
  }

}