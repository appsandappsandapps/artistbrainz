package com.example.swapcard.ui.searchlist

import android.os.Parcelable
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.parcelize.Parcelize

class SearchListUIState(
  private val viewModel: SearchListViewModel,
  private var existing: Values = Values(),
  private val saveToParcel: (Values) -> Unit = {},
) {

  @Parcelize
  data class ArtistUI (
    val id: Int,
    val name: String,
    val bookmarked: Boolean,
  ): Parcelable

  @Parcelize data class Values(
    val artists: List<ArtistUI> = listOf(),
  ): Parcelable

  var valuesFlow = MutableStateFlow(existing)
    private set

  private var values
    get() = valuesFlow.value
    set(value) {
      valuesFlow.value = value
      saveToParcel(values)
    }

  fun setItems(
    artists: List<ArtistUI>,
  ) {
    values = values.copy(
      artists = artists,
    )
  }

}
