package appsandapps.artistbrainz.ui.artistdetail

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import appsandapps.artistbrainz.utils.StateSaver
import appsandapps.artistbrainz.utils.ext.viewModelWithSavedState
import appsandapps.artistbrainz.ui.artistdetail.ArtistDetailAction.*
import appsandapps.artistbrainz.ui.artistdetail.ArtistDetailUIModel
import appsandapps.artistbrainz.ui.artistdetail.ArtistDetailUIValues
import appsandapps.artistbrainz.ui.theme.StandardPadding
import appsandapps.artistbrainz.utils.ext.findActivity
import appsandapps.artistbrainz.utils.ext.gotoUrl

/**
 * - Shows artist info
 * - Search youtube
 * - Open in last.fm
 * - Bookmark
 * - Unbookmark
 */
@Composable
fun ArtistDetailScreen(
  artistId: String,
) {
  val activity = LocalContext.current.findActivity()
  val stateObj = viewModelWithSavedState {
    ArtistDetailViewModel(
      StateSaver(it),
      artistId,
      { activity?.gotoUrl(it) },
    )
  }.uiState
  val values = stateObj.stateFlow.collectAsState().value

  Surface {
    ArtistDetailContent(
      values.artist.name,
      values.artist.disambiguation,
      values.artist.rating.let {
        "Rating ${it.value} (${it.voteCount} votes)"
      },
      values.artist.summary,
      values.artist.bookmarked,
      values.loading,
      values.error,
      { stateObj.update(SearchYoutube()) },
      { stateObj.update(ViewLastFm()) },
      { stateObj.update(Bookmark()) },
      { stateObj.update(Debookmark()) },
    )
  }
}

@Composable
private fun ArtistDetailContent(
  name: String,
  disambiguous: String,
  ratingText: String,
  description: String,
  bookmarked: Boolean,
  loading: Boolean,
  error: String,
  searchYoutube: () -> Unit,
  viewOnLastfm: () -> Unit,
  bookmark: () -> Unit,
  debookmark: () -> Unit,
) {
  val padding = StandardPadding
  val context = LocalContext.current
  if(loading)
    LinearProgressIndicator(
      Modifier.fillMaxWidth(),
    )
  else if(error.isBlank())
    Column(
      Modifier
        .verticalScroll(rememberScrollState())
    ) {
      Row(Modifier.padding(padding)) {
        Checkbox(
          bookmarked,
          { if (it) bookmark() else debookmark() },
        )
        Text(
          name,
          style = MaterialTheme.typography.h4,
        )
      }
      Divider(color = MaterialTheme.colors.onBackground)
      Text(disambiguous, Modifier.padding(padding))
      Divider(color = MaterialTheme.colors.onBackground)
      Text(ratingText, Modifier.padding(padding))
      Button({ searchYoutube() }, Modifier.padding(padding)) {
        Text("Search youtube")
      }
      Button({ viewOnLastfm() }, Modifier.padding(padding)) {
        Text("View on last.fm")
      }
      Text(description, Modifier.padding(padding))
    }
  else
    Toast.makeText(context, error, Toast.LENGTH_LONG).show()
}
