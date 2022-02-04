package com.example.swapcard

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import org.junit.Rule
import org.junit.Test

class FullAppTest {

  @get:Rule
  val composeTestRule = createAndroidComposeRule(com.example.swapcard.MainActivity::class.java)

  @Test
  fun testIfCheckboxTextIsThere() {

    composeTestRule
      .onNodeWithText("Hi three")
      .assertDoesNotExist()

    composeTestRule
      .onNodeWithText("Make background yellow")
      .assertExists()

  }

}