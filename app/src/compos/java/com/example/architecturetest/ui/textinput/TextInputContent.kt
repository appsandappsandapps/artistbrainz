package com.example.architecturetest

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Checkbox
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun TextInputContent(
  inputText: String,
  setInputText: (String) -> Unit,
  yellowBg: Boolean,
  setYellowBg: (Boolean) -> Unit,
  pressEnter: () -> Unit,
  modifier: Modifier = Modifier,
) {
  val background =
    if(yellowBg) Color.Yellow
    else Color.LightGray
  val kbController = LocalSoftwareKeyboardController.current
  Column(modifier) {
    TextField(
      inputText,
      setInputText,
      Modifier
        .fillMaxWidth()
        .background(background),
      placeholder = { Text("Enter text here") },
      keyboardOptions = KeyboardOptions(
        imeAction = ImeAction.Done
      ),
      keyboardActions = KeyboardActions {
        pressEnter()
        kbController?.hide()
      }
    )
    Row {
      Text("Make background yellow")
      Checkbox(
        yellowBg,
        setYellowBg,
      )
    }
  }
}
