package appsandapps.artistbrainz.ui.bookmarks

import androidx.lifecycle.SavedStateHandle
import appsandapps.artistbrainz.Application
import appsandapps.artistbrainz.repositories.ArtistsRepository
import appsandapps.artistbrainz.getByHashCode
import appsandapps.artistbrainz.setByHashCode
import appsandapps.artistbrainz.utils.DispatchedViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect

class BookmarksViewModel(
  application: Application,
  private val savedState: SavedStateHandle,
  public var gotoDetail: (String) -> Unit = {},
  private val repository: ArtistsRepository = application.artistsRepository,
  private var mockUiState: BookmarksUIState? = null,
  dispatcher: CoroutineDispatcher = Dispatchers.IO,
): DispatchedViewModel(dispatcher) {

  val uiState = mockUiState ?:
    BookmarksUIState(
      viewModel = this,
      existing = savedState.getByHashCode(BookmarksUIState.UIValues()),
      saveToParcel = { savedState.setByHashCode(it) }
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
