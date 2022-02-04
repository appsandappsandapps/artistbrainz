package com.example.architecturetest.ui.textlist

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.example.architecturetest.viewModelWithSavedState

@Composable
fun TextListScreen(
  navController: NavHostController,
  modifier: Modifier = Modifier,
) {
  val textListViewModel = viewModelWithSavedState { app, saved ->
    TextListViewModel(
      app,
      saved,
      { navController.navigate("about") }
    )
  }
  var uiState = textListViewModel.uiState
  var values = uiState.valuesFlow.collectAsState().value
  TextListContent(
    values.items,
    uiState::newScreenPress,
    modifier,
  )
}

