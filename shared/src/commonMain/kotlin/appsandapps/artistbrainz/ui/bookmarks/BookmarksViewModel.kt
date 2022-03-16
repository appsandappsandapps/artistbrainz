package appsandapps.artistbrainz.ui.bookmarks

import appsandapps.artistbrainz.ServiceLocator
import appsandapps.artistbrainz.repositories.ArtistsRepository
import appsandapps.artistbrainz.utils.*
import kotlinx.coroutines.CoroutineDispatcher

class BookmarksViewModel(
  private val stateSaver: StateSaveable,
  public var gotoDetail: (String) -> Unit = {},
  private val repository: ArtistsRepository = ServiceLocator.artistsRepo!!,
  private var mockUiModel: BookmarksUIModel? = null,
  dispatcher: CoroutineDispatcher = IODispatcher,
): DispatchedViewModel(dispatcher) {

  constructor(
    gotoDetail: (String) -> Unit = {}
  ) : this(StateSaverEmpty(), gotoDetail)

  val uiModel = mockUiModel ?: BookmarksUIModel(this, stateSaver)

  init {
    observeArtists()
  }

  private fun observeArtists() = dispatchedLaunch {
    repository.bookmarks.collect {
      uiModel.update(BookmarksAction.SetBookmarks(it.bookmarks))
    }
  }

  public fun debookmark(id: String) = dispatchedLaunch {
    repository.debookmark(id)
  }

  public fun gotoDetailScreen(id: String) {
    gotoDetail(id)
  }

}
