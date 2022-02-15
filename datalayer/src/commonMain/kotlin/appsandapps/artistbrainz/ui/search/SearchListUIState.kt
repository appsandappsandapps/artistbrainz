package appsandapps.artistbrainz.ui.search

import kotlinx.coroutines.flow.MutableStateFlow
import appsandapps.artistbrainz.utils.Parcelable
import kotlinx.parcelize.Parcelize

class SearchListUIState(
  private var existing: UIValues = UIValues(),
  private val saveToParcel: (UIValues) -> Unit = {},
  private val searchArtists: (String) -> Unit,
  private val paginateSearch: () -> Unit,
  private val bookmark: (String, String) -> Unit,
  private val debookmark: (String) -> Unit,
  private val gotoArtistDetail: (String) -> Unit,
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

  val valuesFlow = MutableStateFlow(existing)

  private var values
    get() = valuesFlow.value
    set(value) {
      valuesFlow.value = value
      saveToParcel(values)
    }

  // Called via the views / composables

  fun onClearTextPressed() {
    values = values.copy(inputText = "")
  }

  fun onTypedSearchQuery(s: String) {
    values = values.copy(inputText = s)
  }

  fun onPressEnter() {
    values = values.copy(
      loading = true,
      emptyList = false,
      error = "",
      artists = listOf()
    )
    searchArtists(values.inputText)
  }

  fun onPaginateSearch() {
    values = values.copy(loading = true)
    paginateSearch()
  }

  fun onBookmark(id: String, name: String) {
    bookmark(id, name)
  }

  fun onDebookmark(id: String) {
    debookmark(id)
  }

  fun onGotoArtistDetail(id: String) {
    gotoArtistDetail(id)
  }

  // Called via the view model

  fun setError(e: String) {
    values = values.copy(
      loading = false,
      error = e
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

  fun applyBookmarksToArtists(bookmarkIds: List<String>) {
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

  fun setEmptyList() {
    values = values.copy(
      emptyList = true,
      loading = false,
      artists = listOf()
    )
  }

}
