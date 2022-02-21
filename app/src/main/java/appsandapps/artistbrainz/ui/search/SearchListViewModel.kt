package appsandapps.artistbrainz.ui.search

import appsandapps.artistbrainz.ServiceLocator
import appsandapps.artistbrainz.repositories.ArtistsRepository
import appsandapps.artistbrainz.utils.DispatchedViewModel
import kotlinx.coroutines.CoroutineDispatcher
import appsandapps.artistbrainz.ui.search.SearchListUIState.Action.*
import appsandapps.artistbrainz.utils.IODispatcher
import appsandapps.artistbrainz.utils.StateSaver
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect

class SearchListViewModel(
  private val savedState: StateSaver,
  public var gotoDetail: (String) -> Unit = {},
  private val repository: ArtistsRepository = ServiceLocator.artistsRepo,
  private var mockUiState: SearchListUIState ? = null,
  dispatcher: CoroutineDispatcher = IODispatcher,
): DispatchedViewModel(dispatcher) {

  val uiState = mockUiState ?:
    SearchListUIState(
      viewModel = this,
      existing = savedState.get(SearchListUIState.UIValues()),
      saveToParcel = { savedState.save(it) }
    )

  init {
    dispatchedLaunch { observeArtists() }
    dispatchedLaunch { observeBookmarks() }
  }

  private suspend fun observeArtists() {
    repository.searchedForArtists.collect {
      if(it.error.isNotBlank()) {
        uiState.update(ServerError(it.error))
      } else if(!it.paginated && it.artists.size == 0) {
        uiState.update(EmptyResults())
      } else
        uiState.update(AddArtists(it.artists))
    }
  }

  private suspend fun observeBookmarks() {
    repository.bookmarks.collect {
      val ids = it.bookmarks.map { it.id }
      uiState.update(BookmarksMerge(ids))
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
