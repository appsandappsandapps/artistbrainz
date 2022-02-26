package appsandapps.artistbrainz.ui.artistdetail

import appsandapps.artistbrainz.data.Artist
import appsandapps.artistbrainz.ui.artistdetail.ArtistDetailAction.*
import appsandapps.artistbrainz.utils.UIModel
import appsandapps.artistbrainz.utils.Parcelable
import appsandapps.artistbrainz.utils.Parcelize

@Parcelize
data class ArtistDetailUIValues(
  val artist: Artist = Artist(),
  var uiBookmarked: Boolean = false,
  var loading: Boolean = true,
  var error: String = "",
): Parcelable

sealed class ArtistDetailAction {
  class SetBookmarked(val bookmarked: Boolean): ArtistDetailAction()
  class Bookmark: ArtistDetailAction()
  class Debookmark: ArtistDetailAction()
  class SearchYoutube: ArtistDetailAction()
  class ViewLastFm: ArtistDetailAction()
  class SetArtist(val artist: Artist): ArtistDetailAction()
  class ServerError(val error: String): ArtistDetailAction()
}

class ArtistDetailUIModel(
  private val viewModel: ArtistDetailViewModel,
  private var existing: ArtistDetailUIValues = ArtistDetailUIValues(),
  private val saveToParcel: (ArtistDetailUIValues) -> Unit = {},
) : UIModel<ArtistDetailUIValues>(existing, saveToParcel) {

  fun update(action: ArtistDetailAction): Any = when(action) {
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
