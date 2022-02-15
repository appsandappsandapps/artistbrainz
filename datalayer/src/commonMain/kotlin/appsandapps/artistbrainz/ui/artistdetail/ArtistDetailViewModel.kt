package appsandapps.artistbrainz.ui.artistdetail

import appsandapps.artistbrainz.repositories.ArtistsRepository
import appsandapps.artistbrainz.utils.StateSaver
import appsandapps.artistbrainz.utils.DispatchedViewModel
import kotlinx.coroutines.*

class ArtistDetailViewModel(
  private val savedState: StateSaver,
  private val artistId: String,
  public var gotoUrlCallback: (String) -> Unit = {},
  private val repository: ArtistsRepository,
  private var mockUiState: ArtistDetailUIState? = null,
  dispatcher: CoroutineDispatcher,
): DispatchedViewModel(dispatcher) {

  val uiState = mockUiState ?:
    ArtistDetailUIState(
      existing = savedState.get(ArtistDetailUIState.UIValues()),
      saveToParcel = { savedState.save(it) },
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
