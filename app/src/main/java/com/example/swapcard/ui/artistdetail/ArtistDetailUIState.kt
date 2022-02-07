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
    val rating: Double = 0.0,
    val loading: Boolean = false,
    val error: String = "",
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
    values = artist.copy(loading = false, error = "")
  }

  fun bookmark() {
    viewModel.bookmark(values.id, values.name)
  }

  fun debookmark() {
    viewModel.debookmark()
  }

  fun setLoading(b: Boolean) {
    values = values.copy(loading = b, error = "")
  }

  fun setError(e: String) {
    values = values.copy(error = e)
  }

}
