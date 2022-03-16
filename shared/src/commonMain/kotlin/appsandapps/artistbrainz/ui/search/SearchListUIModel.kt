package appsandapps.artistbrainz.ui.search

import appsandapps.artistbrainz.data.Artist
import appsandapps.artistbrainz.ui.search.SearchAction.*
import appsandapps.artistbrainz.utils.*

@Parcelize
data class SearchUIValues(
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

sealed class SearchAction {
  class ClearSearch : SearchAction()
  class TypedSearch(val query: String) : SearchAction()
  class PressSearch : SearchAction()
  class PaginateSearch : SearchAction()
  class Bookmark(val id: String, val name: String): SearchAction()
  class Debookmark(val id: String) : SearchAction()
  class GotoArtistDetail(val id: String) : SearchAction()
  class AddArtists(val artists: List<Artist>) : SearchAction()
  class ServerError(val error: String) : SearchAction()
  class EmptyResults : SearchAction()
  // When we (un)bookmark, update the artists on the screen
  class BookmarksMerge(val ids: List<String>) : SearchAction()
}

class SearchListUIModel(
  private val viewModel: SearchListViewModel,
  private val stateSaver: StateSaveable,
) : UIModel<SearchUIValues>(
  stateSaver.get(SearchUIValues()),
  stateSaver
) {

  fun update(action: SearchAction): Any = when(action) {
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
