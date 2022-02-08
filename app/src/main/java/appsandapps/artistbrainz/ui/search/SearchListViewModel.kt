package appsandapps.artistbrainz.ui.search

import androidx.lifecycle.SavedStateHandle
import appsandapps.artistbrainz.Application
import appsandapps.artistbrainz.repositories.ArtistsRepository
import appsandapps.artistbrainz.getByHashCode
import appsandapps.artistbrainz.setByHashCode
import appsandapps.artistbrainz.utils.DispatchedViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect

class SearchListViewModel(
  application: Application,
  private val savedState: SavedStateHandle,
  public var gotoDetail: (String) -> Unit = {},
  private val repository: ArtistsRepository = application.artistsRepository,
  private var mockUiState: SearchListUIState ? = null,
  dispatcher: CoroutineDispatcher = Dispatchers.IO,
): DispatchedViewModel(dispatcher) {

  val uiState = mockUiState ?:
    SearchListUIState(
      viewModel = this,
      existing = savedState.getByHashCode(SearchListUIState.UIValues()),
      saveToParcel = { savedState.setByHashCode(it) }
    )

  init {
    dispatchedLaunch { observeArtists() }
    dispatchedLaunch { observeBookmarks() }
  }

  private suspend fun observeArtists() {
    repository.searchedForArtists.collect {
      if(it.error.isNotBlank()) {
        uiState.setError(it.error)
      } else if(!it.paginated && it.artists.size == 0) {
        uiState.setEmptyList()
      } else
        uiState.addArtists(it.artists.map {
          SearchListUIState.ArtistUI(it.id, it.name, it.bookmarked)
        })
    }
  }

  private suspend fun observeBookmarks() {
    repository.bookmarks.collect {
      val ids = it.bookmarks.map { it.id }
      uiState.applyBookmarksToArtists(ids)
    }
  }

  public fun paginateSearch() = dispatchedLaunch {
    repository.paginateLastSearch()
  }

  public fun bookmark(id: String, name: String) = dispatchedLaunch {
    repository.bookmark(id, name)
  }

  public fun debookmark(id: String) = dispatchedLaunch {
    repository.debookmark(id)
  }

  public fun gotoArtistDetail(id: String) {
    gotoDetail(id)
  }

  public fun searchArtists(s:String) = dispatchedLaunch {
    repository.search(s)
  }

}
