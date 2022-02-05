package com.example.swapcard.ui.artistdetail

import android.os.Parcelable
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.parcelize.Parcelize

class ArtistDetailUIState(
  private val viewModel: ArtistDetailViewModel,
  private var existing: UIValues = UIValues(),
  private val saveToParcel: (UIValues) -> Unit = {},
) {

  @Parcelize data class UIValues(
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

  fun setArtist(artist: UIValues) {
    values = artist
  }

  fun bookmark() {
    viewModel.bookmark(values.id, values.name)
  }

  fun debookmark() {
    viewModel.debookmark()
  }

}
