package com.example.architecturetest.ui.textlist

import android.os.Parcelable
import com.example.architecturetest.ui.textinput.TextInputViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.parcelize.Parcelize

class TextListUIState(
  private val viewModel: TextListViewModel,
  private var existing: Values = Values(),
  private val saveToParcel: (Values) -> Unit = {},
) {

  @Parcelize data class Values(
    val items: List<String> = listOf(),
  ) : Parcelable

  var valuesFlow = MutableStateFlow(existing)
    private set

  private var values
    get() = valuesFlow.value
    set(value) {
      valuesFlow.value = value
      saveToParcel(values)
    }

  fun setItems(items: List<String>) {
    values = values.copy(items = items)
  }

  fun newScreenPress() {
    viewModel.gotoNewScreen()
  }

}
