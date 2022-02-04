package com.example.architecturetest.ui.textinput

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.example.architecturetest.TextInputContent
import com.example.architecturetest.viewModelWithSavedState

@Composable
fun TextInputScreen(
  navcontroller: NavHostController,
  modifier: Modifier = Modifier,
) {
  val textInputViewModel = viewModelWithSavedState { app, saved ->
    TextInputViewModel(app, saved)
  }
  var uiState = textInputViewModel.uiState
  var values = uiState.valuesFlow.collectAsState().value
  TextInputContent(
    values.inputText,
    uiState::setInputText,
    values.yellowBg,
    uiState::setYellowBg,
    uiState::pressEnter,
    modifier,
  )
}

