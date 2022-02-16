package appsandapps.artistbrainz.ui.bookmarks

import appsandapps.artistbrainz.Application
import appsandapps.artistbrainz.repositories.ArtistsRepository
import appsandapps.artistbrainz.utils.DispatchedViewModel
import appsandapps.artistbrainz.utils.IODispatcher
import appsandapps.artistbrainz.utils.StateSaver
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect

class BookmarksViewModel(
  application: Application,
  private val savedState: StateSaver,
  public var gotoDetail: (String) -> Unit = {},
  private val repository: ArtistsRepository = application.artistsRepository,
  private var mockUiState: BookmarksUIState? = null,
  dispatcher: CoroutineDispatcher = IODispatcher,
): DispatchedViewModel(dispatcher) {

  val uiState = mockUiState ?:
    BookmarksUIState(
      viewModel = this,
      existing = savedState.get(BookmarksUIState.UIValues()),
      saveToParcel = { savedState.save(it) }
    )

  init {
    observeArtists()
  }

  private fun observeArtists() = dispatchedLaunch {
    repository.bookmarks.collect {
      uiState.update(BookmarksUIState.Action.SetBookmarks(it.bookmarks))
    }
  }

  public fun debookmark(id: String) = dispatchedLaunch {
    repository.debookmark(id)
  }

  public fun gotoDetailScreen(id: String) {
    gotoDetail(id)
  }

}
