package appsandapps.artistbrainz.ui.artistdetail

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect
import appsandapps.artistbrainz.ServiceLocator
import appsandapps.artistbrainz.repositories.ArtistsRepository
import appsandapps.artistbrainz.ui.artistdetail.ArtistDetailUIState.Action.*
import appsandapps.artistbrainz.utils.*

class ArtistDetailViewModel(
  savedState: StateSavable,
  private val artistId: String,
  public var gotoUrlCallback: (String) -> Unit = {},
  private val repository: ArtistsRepository = ServiceLocator.artistsRepo,
  private var mockUiState: ArtistDetailUIState? = null,
  dispatcher: CoroutineDispatcher = IODispatcher,
): DispatchedViewModel(dispatcher) {

  constructor(
    artistId: String,
    gotoUrlCallback: (String) -> Unit = {},
  ) : this(StateSaverEmpty(), artistId, gotoUrlCallback)

  val uiState = mockUiState ?:
    ArtistDetailUIState(
      viewModel = this,
      existing = savedState.get(ArtistDetailUIState.UIValues()),
      saveToParcel = { savedState.save(it) }
    )

  init {
    observeArtist()
  }

  private fun observeArtist() = dispatchedLaunch {
    repository.artist(artistId)
    repository.artist.collect {
      if(it.error.isNotBlank()) {
        uiState.update(ServerError(it.error))
      } else {
        uiState.update(SetArtist(it))
      }
    }
  }

  public fun bookmark(id: String, name: String) = dispatchedLaunch {
    repository.bookmark(id, name)
  }

  public fun debookmark() = dispatchedLaunch {
    repository.debookmark(artistId)
  }

  public fun viewOnLastFm(url: String) {
    gotoUrlCallback(url)
  }

  public fun searchYoutube(query: String) {
    gotoUrlCallback("https://www.youtube.com/results?search_query=${query}")
  }

}
