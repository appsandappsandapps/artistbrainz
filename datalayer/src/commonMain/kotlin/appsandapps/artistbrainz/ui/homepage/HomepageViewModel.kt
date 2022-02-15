package appsandapps.artistbrainz.ui.homepage

import appsandapps.artistbrainz.repositories.ArtistsRepository
import appsandapps.artistbrainz.utils.StateSaver
import appsandapps.artistbrainz.utils.DispatchedViewModel
import kotlinx.coroutines.CoroutineDispatcher

class HomepageViewModel(
  private val savedState: StateSaver,
  private val repository: ArtistsRepository,
  private var mockUiState: HomepageUIState? = null,
  dispatcher: CoroutineDispatcher,
): DispatchedViewModel(dispatcher) {

  val uiState = mockUiState ?:
    HomepageUIState(
      existing = savedState.get(HomepageUIState.UIValues()),
      saveToParcel = { savedState.save(it) },
    )

  init {
    observeArtists()
  }

  private fun observeArtists() = dispatchedLaunch {
    repository.bookmarks.collect {
      var bookmarked = it.bookmarks.size
      uiState.setBookmarked(bookmarked)
    }
  }

}
