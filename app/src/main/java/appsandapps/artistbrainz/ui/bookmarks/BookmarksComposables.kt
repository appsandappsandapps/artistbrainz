package appsandapps.artistbrainz.ui.bookmarks

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Checkbox
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import appsandapps.artistbrainz.NavControllerLocal
import appsandapps.artistbrainz.data.Bookmark
import appsandapps.artistbrainz.utils.StateSaver
import appsandapps.artistbrainz.utils.ext.viewModelWithSavedState
import appsandapps.artistbrainz.ui.bookmarks.BookmarksUIState.Action.*

/**
 * - Show bookmarks
 * - Click to unbookmark
 * - Click to see artist details
 */
@Composable
fun BookmarksScreen() {
  val controller = NavControllerLocal.current
  val gotoDetail: (String) -> Unit = { controller.navigate("artistdetail/"+it) }
  // View Model and UI State
  val viewModel = viewModelWithSavedState {
    appsandapps.artistbrainz.ui.bookmarks.BookmarksViewModel(
      StateSaver(it),
      gotoDetail
    )
  }
  viewModel.gotoDetail = gotoDetail // else crash due to hanging onto fragment
  val stateObj = viewModel.uiState
  val stateValues = stateObj.stateFlow.collectAsState().value

  BookmarksContent(
    stateValues.bookmarks,
    BookmarkRowWithState(stateObj),
  )
}

@Composable
private fun BookmarksContent(
  bookmarks: List<Bookmark>,
  bookmarkRowSlot: @Composable (id: String, name: String) -> Unit,
) {
  Column(
    Modifier.fillMaxSize()
  ) {
    LazyColumn {
      items(bookmarks) {
        key(it.id) {
          bookmarkRowSlot(it.id, it.name)
        }
      }
    }
  }
}

@Composable
private fun BookmarkRowWithState(
  stateObj: appsandapps.artistbrainz.ui.bookmarks.BookmarksUIState,
): @Composable (id: String, name: String) -> Unit =
  { id, name ->
    BookmarkRow(
      id,
      name,
      { stateObj.update(Debookmark(id)) },
      { stateObj.update(GotoDetail(id)) }
    )
  }

@Composable
private fun BookmarkRow(
  id: String,
  name: String,
  debookmark: () -> Unit,
  gotodetail: () -> Unit,
) {
  Row(
    Modifier.fillMaxWidth(),
    verticalAlignment = Alignment.CenterVertically,
  ) {
    DeBookmarker(debookmark)
    Text(
      name,
      Modifier
        .clickable { gotodetail() }
        .weight(1F)
        .fillMaxHeight()
    )
  }
}

@Composable
private fun DeBookmarker(
  debookmark: () -> Unit
) {
  var checked by remember { mutableStateOf(true) }
  Checkbox(
    checked,
    {
      checked = false
      debookmark()
    }
  )
}
