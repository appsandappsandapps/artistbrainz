package com.example.architecturetest

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import org.junit.Rule
import org.junit.Test

class CoolTextComposableTest {

  @get:Rule
  val composeTestRule = createComposeRule()

  @Test
  fun testIfCheckboxTextIsThere() {
    composeTestRule.setContent {
      TextInputContent(
        "",
        {},
        false,
        {},
        {}
      )
    }

    composeTestRule
      .onNodeWithText("Hi three")
      .assertDoesNotExist()

    /*
    composeTestRule
      .onNodeWithText("3")
      .performClick()
     */

    composeTestRule
      .onNodeWithText("Make background yellow")
      .assertExists()

  }

}