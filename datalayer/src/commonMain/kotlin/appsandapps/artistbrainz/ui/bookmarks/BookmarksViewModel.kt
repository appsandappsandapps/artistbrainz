package appsandapps.artistbrainz.ui.bookmarks

import appsandapps.artistbrainz.repositories.ArtistsRepository
import appsandapps.artistbrainz.utils.StateSaver
import appsandapps.artistbrainz.utils.DispatchedViewModel
import kotlinx.coroutines.CoroutineDispatcher

class BookmarksViewModel(
  private val savedState: StateSaver,
  public var gotoDetail: (String) -> Unit = {},
  private val repository: ArtistsRepository,
  private var mockUiState: BookmarksUIState? = null,
  dispatcher: CoroutineDispatcher,
): DispatchedViewModel(dispatcher) {

  val uiState = mockUiState ?:
    BookmarksUIState(
      existing = savedState.get(BookmarksUIState.UIValues()),
      saveToParcel = { savedState.save(it) },
      { debookmark(it) },
      { gotoDetailScreen(it) },
    )

  init {
    observeArtists()
  }

  private fun observeArtists() = dispatchedLaunch {
    repository.bookmarks.collect {
      val bookmarks = it.bookmarks.map {
        BookmarksUIState.BookmarkUI(
          it.id,
          it.name,
        )
      }
      uiState.setBookmarks(BookmarksUIState.UIValues(bookmarks))
    }
  }

  public fun debookmark(id: String) = dispatchedLaunch {
    repository.debookmark(id)
  }

  public fun gotoDetailScreen(id: String) {
    gotoDetail(id)
  }

}
