package appsandapps.artistbrainz.ui.artistdetail

import kotlinx.coroutines.*
import appsandapps.artistbrainz.ServiceLocator
import appsandapps.artistbrainz.repositories.ArtistsRepository
import appsandapps.artistbrainz.ui.artistdetail.ArtistDetailAction.*
import appsandapps.artistbrainz.utils.*

class ArtistDetailViewModel(
  stateSaver: StateSaveable,
  private val artistId: String,
  public var gotoUrlCallback: (String) -> Unit = {},
  private val repository: ArtistsRepository = ServiceLocator.artistsRepo!!,
  private var mockUiModel: ArtistDetailUIModel? = null,
  dispatcher: CoroutineDispatcher = IODispatcher,
): DispatchedViewModel(dispatcher) {

  constructor(
    artistId: String,
    gotoUrlCallback: (String) -> Unit = {},
  ) : this(StateSaverEmpty(), artistId, gotoUrlCallback)

  val uiModel = mockUiModel ?: ArtistDetailUIModel(this, stateSaver)

  init {
    observeArtist()
    observeBookmarks()
  }

  private fun observeArtist() = dispatchedLaunch {
    repository.artist(artistId)
    repository.artist.collect {
      if(it.error.isNotBlank()) {
        uiModel.update(ServerError(it.error))
      } else {
        uiModel.update(SetArtist(it))
      }
    }
  }

  private fun observeBookmarks() = dispatchedLaunch {
    repository.bookmarks.collect {
      val currentArtist = it.bookmarks.filter { it.id == artistId }
      if(currentArtist.size == 1) {
        uiModel.update(SetBookmarked(true))
      } else {
        uiModel.update(SetBookmarked(false))
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
