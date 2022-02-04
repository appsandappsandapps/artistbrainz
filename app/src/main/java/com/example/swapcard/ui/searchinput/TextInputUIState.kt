package com.example.swapcard.ui.searchinput

import android.os.Parcelable
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.parcelize.Parcelize

class TextInputUIState(
  private val viewModel: TextInputViewModel,
  private var existing: Values = Values(),
  private val saveToParcel: (Values) -> Unit = {},
) {

  @Parcelize data class Values(
    var inputText: String = "",
  ) : Parcelable

  var valuesFlow = MutableStateFlow(existing)
    private set

  private var values
    get() = valuesFlow.value
    set(value) {
      valuesFlow.value = value
      saveToParcel(values)
    }

  fun setInputText(s: String) {
    values = values.copy(inputText = s)
  }

  fun pressEnter() {
    viewModel.addText(values.inputText)
    values = values.copy(inputText = "")
  }

}