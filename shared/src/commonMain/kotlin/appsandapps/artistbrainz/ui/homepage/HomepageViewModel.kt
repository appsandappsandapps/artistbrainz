package appsandapps.artistbrainz.ui.homepage

import kotlinx.coroutines.CoroutineDispatcher
import appsandapps.artistbrainz.ServiceLocator
import appsandapps.artistbrainz.repositories.ArtistsRepository
import appsandapps.artistbrainz.ui.homepage.HomepageAction.*
import appsandapps.artistbrainz.utils.*

class HomepageViewModel(
  private val savedState: StateSaveable,
  private val repository: ArtistsRepository = ServiceLocator.artistsRepo!!,
  private var mockUiModel: HomepageUIModel? = null,
  dispatcher: CoroutineDispatcher = IODispatcher,
): DispatchedViewModel(dispatcher) {

  constructor() : this(StateSaverEmpty())

  val uiState = mockUiModel ?:
    HomepageUIModel(
      viewModel = this,
      existing = savedState.get(HomepageUIValues()),
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
