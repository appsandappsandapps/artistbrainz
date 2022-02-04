package com.example.swapcard.ui.artistdetail

import android.os.Parcelable
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.parcelize.Parcelize

class ArtistDetailUIState(
  private val viewModel: ArtistDetailViewModel,
  private var existing: Values = Values(),
  private val saveToParcel: (Values) -> Unit = {},
) {

  @Parcelize data class Values(
    val id: String = "",
    val name: String = "",
    val bookmarked: Boolean = false,
    val disambiguation: String = "",
    val voteCount: Int = 0,
    val rating: Int = 0,
  ): Parcelable

  var valuesFlow = MutableStateFlow(existing)
    private set

  private var values
    get() = valuesFlow.value
    set(value) {
      valuesFlow.value = value
      saveToParcel(values)
    }

  fun setArtist(artist: Values) {
    values = artist
  }

}
