package com.example.swapcard.ui.homepage

import android.os.Parcelable
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.parcelize.Parcelize

class HomepageUIState(
  private val viewModel: HomepageViewModel,
  private var existing: UIValues = UIValues(),
  private val saveToParcel: (UIValues) -> Unit = {},
) {

  @Parcelize data class UIValues(
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
    values = UIValues(num)
  }

}
