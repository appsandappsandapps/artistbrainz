package com.example.swapcard.ui.bookmarks

import android.os.Parcelable
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.parcelize.Parcelize

class BookmarksUIState(
  private val viewModel: BookmarksViewModel,
  private var existing: Values = Values(),
  private val saveToParcel: (Values) -> Unit = {},
) {

  @Parcelize
  data class BookmarkUI (
    val id: Int,
    val name: String,
  ): Parcelable

  @Parcelize data class Values(
    val bookmarks: List<BookmarkUI> = listOf(),
  ): Parcelable

  var valuesFlow = MutableStateFlow(existing)
    private set

  private var values
    get() = valuesFlow.value
    set(value) {
      valuesFlow.value = value
      saveToParcel(values)
    }

  fun setItems(bookmarks: Values) {
    values = bookmarks
  }

}
