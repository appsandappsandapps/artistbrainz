package appsandapps.artistbrainz.ui.homepage

import androidx.lifecycle.SavedStateHandle
import appsandapps.artistbrainz.Application
import appsandapps.artistbrainz.repositories.ArtistsRepository
import appsandapps.artistbrainz.getByHashCode
import appsandapps.artistbrainz.setByHashCode
import appsandapps.artistbrainz.utils.DispatchedViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import appsandapps.artistbrainz.ui.homepage.HomepageUIState.Action.*

class HomepageViewModel(
  application: Application,
  private val savedState: SavedStateHandle,
  private val repository: ArtistsRepository = application.artistsRepository,
  private var mockUiState: HomepageUIState ? = null,
  dispatcher: CoroutineDispatcher = Dispatchers.IO,
): DispatchedViewModel(dispatcher) {

  val uiState = mockUiState ?:
    HomepageUIState(
      viewModel = this,
      existing = savedState.getByHashCode(HomepageUIState.UIValues()),
      saveToParcel = { savedState.setByHashCode(it) }
    )

  init {
    observeArtists()
  }

  private fun observeArtists() = dispatchedLaunch {
    repository.bookmarks.collect {
      uiState.update(Bookmarks(it.bookmarks.size))
    }
  }

}
