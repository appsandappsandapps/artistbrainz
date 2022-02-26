package appsandapps.artistbrainz.ui.artistdetail

import appsandapps.artistbrainz.data.Artist
import appsandapps.artistbrainz.ui.artistdetail.Action.*
import appsandapps.artistbrainz.utils.UIModel
import appsandapps.artistbrainz.utils.Parcelable
import appsandapps.artistbrainz.utils.Parcelize

@Parcelize
data class UIValues(
  val artist: Artist = Artist(),
  var uiBookmarked: Boolean = false,
  var loading: Boolean = true,
  var error: String = "",
): Parcelable

sealed class Action {
  class SetBookmarked(val bookmarked: Boolean): Action()
  class Bookmark: Action()
  class Debookmark: Action()
  class SearchYoutube: Action()
  class ViewLastFm: Action()
  class SetArtist(val artist: Artist): Action()
  class ServerError(val error: String): Action()
}

class ArtistDetailUIModel(
  private val viewModel: ArtistDetailViewModel,
  private var existing: UIValues = UIValues(),
  private val saveToParcel: (UIValues) -> Unit = {},
) : UIModel<UIValues>(existing, saveToParcel) {

  fun update(action: Action): Any = when(action) {
    is SetBookmarked -> {
      val artist = stateData.artist.copy(bookmarked = action.bookmarked)
      stateData = stateData.copy(artist = artist)
    }
    is Bookmark -> {
      viewModel.bookmark(stateData.artist.id, stateData.artist.name)
    }
    is Debookmark -> {
      viewModel.debookmark()
    }
    is SearchYoutube -> {
      viewModel.searchYoutube(stateData.artist.name)
    }
    is ViewLastFm -> {
      viewModel.viewOnLastFm(stateData.artist.lastFMUrl)
    }
    is SetArtist -> {
      stateData = stateData.copy(
        artist = action.artist,
        loading = false,
        error = "",
      )
    }
    is ServerError -> {
      stateData = stateData.copy(error = action.error, loading = false)
    }
  }

}
