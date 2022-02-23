package appsandapps.artistbrainz.ui.bookmarks

import appsandapps.artistbrainz.ServiceLocator
import appsandapps.artistbrainz.repositories.ArtistsRepository
import appsandapps.artistbrainz.utils.*
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.collect

class BookmarksViewModel(
  private val savedState: StateSaveable,
  public var gotoDetail: (String) -> Unit = {},
  private val repository: ArtistsRepository = ServiceLocator.artistsRepo!!,
  private var mockUiState: BookmarksUIState? = null,
  dispatcher: CoroutineDispatcher = IODispatcher,
): DispatchedViewModel(dispatcher) {

  constructor(
    gotoDetail: (String) -> Unit = {}
  ) : this(StateSaverEmpty(), gotoDetail)

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
