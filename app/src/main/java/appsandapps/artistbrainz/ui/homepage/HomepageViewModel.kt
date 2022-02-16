package appsandapps.artistbrainz.ui.homepage

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import appsandapps.artistbrainz.Application
import appsandapps.artistbrainz.repositories.ArtistsRepository
import appsandapps.artistbrainz.utils.DispatchedViewModel
import appsandapps.artistbrainz.ui.homepage.HomepageUIState.Action.*
import appsandapps.artistbrainz.utils.IODispatcher
import appsandapps.artistbrainz.utils.StateSaver

class HomepageViewModel(
  application: Application,
  private val savedState: StateSaver,
  private val repository: ArtistsRepository = application.artistsRepository,
  private var mockUiState: HomepageUIState ? = null,
  dispatcher: CoroutineDispatcher = IODispatcher,
): DispatchedViewModel(dispatcher) {

  val uiState = mockUiState ?:
    HomepageUIState(
      viewModel = this,
      existing = savedState.get(HomepageUIState.UIValues()),
      saveToParcel = { savedState.save(it) }
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
