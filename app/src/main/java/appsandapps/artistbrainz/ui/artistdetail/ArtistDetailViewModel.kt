package appsandapps.artistbrainz.ui.artistdetail

import androidx.lifecycle.SavedStateHandle
import appsandapps.artistbrainz.Application
import appsandapps.artistbrainz.repositories.ArtistsRepository
import appsandapps.artistbrainz.getByHashCode
import appsandapps.artistbrainz.setByHashCode
import appsandapps.artistbrainz.utils.DispatchedViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect

class ArtistDetailViewModel(
  application: Application,
  savedState: SavedStateHandle,
  private val artistId: String,
  public var gotoUrlCallback: (String) -> Unit = {},
  private val repository: ArtistsRepository = application.artistsRepository,
  private var mockUiState: ArtistDetailUIState? = null,
  dispatcher: CoroutineDispatcher = Dispatchers.IO,
): DispatchedViewModel(dispatcher) {

  val uiState = mockUiState ?:
    ArtistDetailUIState(
      existing = savedState.getByHashCode(ArtistDetailUIState.UIValues()),
      saveToParcel = { savedState.setByHashCode(it) },
      { id, name -> bookmark(id, name) },
      { debookmark() },
      { gotoUrlCallback(it) },
      { searchYoutube(it) },
    )

  init {
    observeArtist()
  }

  private fun observeArtist() = dispatchedLaunch {
    repository.artist(artistId)
    repository.artist.collect {
      if(it.error.isNotBlank()) {
        uiState.setError(it.error)
      } else {
        var artist = it
        uiState.setArtist(
          artist.id,
          artist.name,
          artist.bookmarked,
          artist.disambiguation,
          artist.rating.value,
          artist.rating.voteCount,
          artist.summary,
          artist.lastFMUrl,
        )
      }
    }
  }

  public fun bookmark(id: String, name: String) = dispatchedLaunch {
    repository.bookmark(id, name)
  }

  public fun debookmark() = dispatchedLaunch {
    repository.debookmark(artistId)
  }

  public fun searchYoutube(query: String) {
    gotoUrlCallback("https://www.youtube.com/results?search_query=${query}")
  }

}
