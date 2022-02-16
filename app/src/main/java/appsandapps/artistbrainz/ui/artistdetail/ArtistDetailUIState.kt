package appsandapps.artistbrainz.ui.artistdetail

import android.os.Parcelable
import appsandapps.artistbrainz.data.Artist
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.parcelize.Parcelize
import appsandapps.artistbrainz.ui.artistdetail.ArtistDetailUIState.Action.*

class ArtistDetailUIState(
  private val viewModel: ArtistDetailViewModel,
  private var existing: UIValues = UIValues(),
  private val saveToParcel: (UIValues) -> Unit = {},
) {

  val valuesFlow = MutableStateFlow(existing)

  private var stateData
    get() = valuesFlow.value
    set(value) {
      valuesFlow.value = value
      saveToParcel(stateData)
    }

  @Parcelize data class UIValues(
    val artist: Artist = Artist(),
    var uiBookmarked: Boolean = false,
    var loading: Boolean = true,
    var error: String = "",
  ): Parcelable

  sealed class Action {
    class Bookmark: Action()
    class Debookmark: Action()
    class SearchYoutube: Action()
    class ViewLastFm: Action()
    class SetArtist(val artist: Artist): Action()
    class ServerError(val error: String): Action()
  }

  fun update(action: Action) = when(action) {
    is Bookmark -> {
      stateData = stateData.copy(uiBookmarked = true)
      viewModel.bookmark(stateData.artist.id, stateData.artist.name)
    }
    is Debookmark -> {
      stateData = stateData.copy(uiBookmarked = false)
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
        uiBookmarked = action.artist.bookmarked,
        loading = false,
        error = "",
      )
    }
    is ServerError -> {
      stateData = stateData.copy(error = action.error, loading = false)
    }
  }

}
