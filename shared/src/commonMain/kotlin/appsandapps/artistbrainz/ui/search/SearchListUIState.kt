package appsandapps.artistbrainz.ui.search

import appsandapps.artistbrainz.data.Artist
import appsandapps.artistbrainz.ui.search.SearchListUIState.Action.*
import appsandapps.artistbrainz.ui.search.SearchListUIState.UIValues
import appsandapps.artistbrainz.utils.Parcelable
import appsandapps.artistbrainz.utils.Parcelize
import appsandapps.artistbrainz.utils.UIState

class SearchListUIState(
  private val viewModel: SearchListViewModel,
  private var existing: UIValues = UIValues(),
  private val saveToParcel: (UIValues) -> Unit = {},
) : UIState<UIValues>(existing, saveToParcel){

  @Parcelize data class UIValues(
    val loading: Boolean = false,
    val hasNoResults: Boolean = false,
    val isBeforeFirstSearch: Boolean = true,
    val error: String = "",
    val inputText: String = "",
    val artists: List<Artist> = listOf(),
  ): Parcelable {
    fun showClearText():Boolean =
      inputText.isNotBlank() && loading == false
  }

  sealed class Action {
    class ClearSearch : Action()
    class TypedSearch(val query: String) : Action()
    class PressSearch : Action()
    class PaginateSearch : Action()
    class Bookmark(val id: String, val name: String): Action()
    class Debookmark(val id: String) : Action()
    class GotoArtistDetail(val id: String) : Action()
    class AddArtists(val artists: List<Artist>) : Action()
    class ServerError(val error: String) : Action()
    class EmptyResults : Action()
    // When we (un)bookmark, update the artists on the screen
    class BookmarksMerge(val ids: List<String>) : Action()
  }

  fun update(action: Action): Any = when(action) {
    is ClearSearch -> {
      stateData = stateData.copy(inputText = "")
    }
    is TypedSearch -> {
      stateData = stateData.copy(inputText = action.query)
    }
    is PressSearch -> {
      stateData = stateData.copy(
        loading = true,
        hasNoResults = false,
        error = "",
        artists = listOf()
      )
      viewModel.searchArtists(stateData.inputText)
    }
    is PaginateSearch -> {
      stateData = stateData.copy(loading = true)
      viewModel.paginateSearch()
    }
    is Bookmark -> {
      viewModel.bookmark(action.id, action.name)
    }
    is Debookmark -> {
      viewModel.debookmark(action.id)
    }
    is GotoArtistDetail -> {
      viewModel.gotoArtistDetail(action.id)
    }
    is AddArtists -> {
      stateData = stateData.copy(
        error = "",
        isBeforeFirstSearch = false,
        hasNoResults = false,
        loading = false,
        artists = stateData.artists + action.artists,
      )
    }
    is ServerError -> {
      stateData = stateData.copy(
        loading = false,
        error = action.error
      )
    }
    is EmptyResults -> {
      stateData = stateData.copy(
        hasNoResults = true,
        loading = false,
        artists = listOf()
      )
    }
    is BookmarksMerge -> {
      applyBookmarksToArtists(action.ids)
    }
  }

  private fun applyBookmarksToArtists(bookmarkIds: List<String>) {
    val artists = stateData.artists
    val newArtists = artists.map {
      if(bookmarkIds.contains(it.id)) {
        it.copy(bookmarked = true)
      } else {
        it.copy(bookmarked = false)
      }
    }
    stateData = stateData.copy(artists = newArtists)
  }

}