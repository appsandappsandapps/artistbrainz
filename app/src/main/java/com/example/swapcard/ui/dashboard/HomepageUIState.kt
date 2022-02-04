package com.example.swapcard.ui.dashboard

import android.os.Parcelable
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.parcelize.Parcelize

class HomepageUIState(
  private val viewModel: HomepageViewModel,
  private var existing: Values = Values(),
  private val saveToParcel: (Values) -> Unit = {},
) {

  @Parcelize data class Values(
    val bookmarked: Int = 0,
  ): Parcelable

  var valuesFlow = MutableStateFlow(existing)
    private set

  private var values
    get() = valuesFlow.value
    set(value) {
      valuesFlow.value = value
      saveToParcel(values)
    }

  fun setBookmarked(num: Int) {
    values = Values(num)
  }

}
