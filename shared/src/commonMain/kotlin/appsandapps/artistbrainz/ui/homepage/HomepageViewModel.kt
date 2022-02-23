package appsandapps.artistbrainz.ui.homepage

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.collect
import appsandapps.artistbrainz.ServiceLocator
import appsandapps.artistbrainz.repositories.ArtistsRepository
import appsandapps.artistbrainz.ui.homepage.HomepageUIState.Action.*
import appsandapps.artistbrainz.utils.*

class HomepageViewModel(
  private val savedState: StateSaveable,
  private val repository: ArtistsRepository = ServiceLocator.artistsRepo!!,
  private var mockUiState: HomepageUIState? = null,
  dispatcher: CoroutineDispatcher = IODispatcher,
): DispatchedViewModel(dispatcher) {

  constructor() : this(StateSaverEmpty())

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
