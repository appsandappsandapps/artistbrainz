package com.example.swapcard.ui.search

import android.os.Parcelable
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.parcelize.Parcelize

class SearchListUIState(
  private val viewModel: SearchListViewModel,
  private var existing: UIValues = UIValues(),
  private val saveToParcel: (UIValues) -> Unit = {},
) {

  @Parcelize
  data class ArtistUI (
    val id: String,
    val name: String,
    val bookmarked: Boolean,
  ): Parcelable

  @Parcelize data class UIValues(
    var loading: Boolean = false,
    var emptyList: Boolean = false,
    var error: String = "",
    var inputText: String = "",
    val artists: List<ArtistUI> = listOf(),
  ): Parcelable {
    fun showClearText():Boolean =
      inputText.isNotBlank() && loading == false
  }

  var valuesFlow = MutableStateFlow(existing)
    private set

  private var values
    get() = valuesFlow.value
    set(value) {
      valuesFlow.value = value
      saveToParcel(values)
    }

  fun setError(e: String) {
    values = values.copy(
      loading = false,
      error = e
    )
  }

  fun clearArtists() {
    values = values.copy(
      error = "",
      artists = listOf()
    )
  }

  fun addArtists(
    newArtists: List<ArtistUI>,
  ) {
    values = values.copy(
      error = "",
      emptyList = false,
      loading = false,
      artists = values.artists + newArtists,
    )
  }

  fun applyBookmarks(bookmarkIds: List<String>) {
    val artists = values.artists
    val newArtists = artists.map {
      if(bookmarkIds.contains(it.id)) {
        it.copy(bookmarked = true)
      } else {
        it.copy(bookmarked = false)
      }
    }
    values = values.copy(artists = newArtists)
  }

  fun setInputText(s: String) {
    values = values.copy(inputText = s)
  }

  fun pressEnter() {
    clearArtists()
    values = values.copy(loading = true, emptyList = false)
    viewModel.searchArtists(values.inputText)
  }

  fun setLoading(b: Boolean) {
    values = values.copy(loading = b, emptyList = false)
  }

  fun setEmptyList() {
    values = values.copy(
      emptyList = true,
      loading = false,
      artists = listOf()
    )
  }

  public fun paginateSearch() {
    viewModel.paginateSearch()
  }

  public fun bookmark(id: String, name: String) {
    viewModel.bookmark(id, name)
  }

  public fun debookmark(id: String) {
    viewModel.debookmark(id)
  }

  public fun gotoArtistDetail(id: String) {
    viewModel.gotoArtistDetail(id)
  }

}
