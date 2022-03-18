package appsandapps.artistbrainz.ui.search

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import appsandapps.artistbrainz.NavControllerLocal
import appsandapps.artistbrainz.data.Artist
import appsandapps.artistbrainz.utils.StateSaver
import appsandapps.artistbrainz.utils.ext.viewModelWithSavedState
import appsandapps.artistbrainz.ui.search.SearchAction.*
import appsandapps.artistbrainz.ui.theme.StandardPadding
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * - Searches artists
 * - Shows loading
 * - Shows error
 * - Can clear text input
 * - Click to goto artist detail
 * - Click to bookmark
 * - Unclick to bookmark
 */
@Composable
fun SearchListScreen() {
  val controller = NavControllerLocal.current
  val gotoDetail: (String) -> Unit = {
    controller.navigate("artistdetail/"+it)
  }
  // View Model & UI State
  val viewModel = viewModelWithSavedState {
    SearchListViewModel(
      StateSaver(it),
      gotoDetail,
    )
  }
  viewModel.gotoDetail = gotoDetail // else crash -- keeping onto old fragment
  val stateObj = viewModel.uiModel
  val uiValues = stateObj.stateFlow.collectAsState().value

  SearchListContent(
    SearchResultsWithState(stateObj, uiValues),
    SearchInputWithState(stateObj, uiValues),
    uiValues.error,
    uiValues.hasNoResults,
    uiValues.isBeforeFirstSearch,
    clearError = { stateObj.update(ServerError("")) },
  )

}

/**
 * Shows artists list
 * and any errors and if there's no results
 */
@Composable
private fun SearchListContent(
  resultsSlot: @Composable () -> Unit,
  inputSlot: @Composable () -> Unit,
  error: String,
  emptyList: Boolean,
  beforeFirstSearch: Boolean,
  clearError: () -> Unit,
) {
  val scope = rememberCoroutineScope()
  val context = LocalContext.current
    Column {
      inputSlot()
      if(emptyList && !beforeFirstSearch)
        Text("No search results!!")
      else
        resultsSlot()
    }
  if(error.isNotBlank()) {
    Toast.makeText(context, error, Toast.LENGTH_LONG).show()
    scope.launch {
      delay(400)
      clearError()
    }
  }
}

@Composable
private fun SearchResultsWithState(
  stateObj: SearchListUIModel,
  uiValues: SearchUIValues,
): @Composable () -> Unit = {
  SearchResults(
    uiValues.artists,
    ArtistRowWithState(stateObj),
    { stateObj.update(PaginateSearch()) },
  )
}

/**
 * When we're at the end of the
 * artists list call paginate()
 */
@Composable
private fun SearchResults(
  artists: List<Artist>,
  artistRowSlot: @Composable (id: String, name: String, boolean: Boolean) -> Unit,
  paginate: () -> Unit,
) {
  LazyColumn {
    itemsIndexed(artists) { index, artist ->
      if (index + 1 == artists.size)
        LaunchedEffect(Unit) {
          paginate()
        }
      key(artist.id, artist.bookmarked) {
        artistRowSlot(artist.id, artist.name, artist.bookmarked)
      }
    }
  }
}

private fun SearchInputWithState(
  stateObj: SearchListUIModel,
  uiValues: SearchUIValues,
 ): @Composable () -> Unit = {
  SearchInput(
    uiValues.inputText,
    uiValues.loading,
    { stateObj.update(TypedSearch(it)) },
    { stateObj.update(PressSearch()) },
    { stateObj.update(ClearSearch()) },
  )
}

/**
 * Shows loading spinner
 * Has clear input icon
 */
@Composable
private fun SearchInput(
  searchInput: String,
  loading: Boolean,
  typeText: (String) -> Unit,
  pressEnter: () -> Unit,
  pressClear: () -> Unit,
) {
  Box {
    if(loading)
      CircularProgressIndicator(
        Modifier.align(Alignment.CenterEnd),
      )
    SearchInputTextField(
      searchInput,
      typeText,
      pressEnter,
    )
    if(searchInput.isNotBlank() && !loading)
      Text(
        "✘",
        Modifier
          .clickable { pressClear() }
          .padding(end = StandardPadding)
          .align(Alignment.CenterEnd),
      )
  }
}

/**
 * Closes keyboard when search is pressed
 */
@OptIn(ExperimentalComposeUiApi::class)
@Composable
private fun SearchInputTextField(
  searchInput: String,
  typeText: (String) -> Unit,
  pressEnter: () -> Unit,
) {
  val kbController = LocalSoftwareKeyboardController.current
  TextField(
    searchInput,
    { typeText(it) },
    Modifier.fillMaxWidth(),
    placeholder = { Text("Search for an artist...") },
    keyboardOptions = KeyboardOptions(
      imeAction = ImeAction.Search
    ),
    keyboardActions = KeyboardActions {
      pressEnter()
      kbController?.hide()
    }
  )
}

private fun ArtistRowWithState(
  stateObj: SearchListUIModel,
): @Composable (String, String, Boolean) -> Unit =
  { id, name, bookmarked ->
    ArtistRow(
      id,
      name,
      bookmarked,
      { id, name -> stateObj.update(Bookmark(id, name)) },
      { stateObj.update(Debookmark(it)) },
      { stateObj.update(GotoArtistDetail(id)) },
    )
  }

/**
 * Includes ability to (un)bookmark
 */
@Composable
private fun ArtistRow(
  id: String,
  name: String,
  bookmarked: Boolean,
  bookmark: (String, String) -> Unit,
  debookmark: (String) -> Unit,
  gotodetail: (String) -> Unit,
) {
  Row(
    Modifier.fillMaxWidth(),
    verticalAlignment = Alignment.CenterVertically,
  ) {
    ArtistBookmarker(
      id,
      name,
      bookmarked,
      bookmark,
      debookmark,
    )
    Text(
      name,
      Modifier
        .clickable { gotodetail(id) }
        .weight(1F)
        .fillMaxHeight()
    )
  }
}

@Composable
private fun ArtistBookmarker(
  id: String,
  name: String,
  bookmarked: Boolean,
  bookmark: (String, String) -> Unit,
  debookmark: (String) -> Unit
) {
  var checked by remember { mutableStateOf(bookmarked) }
  Checkbox(
    checked,
    {
      checked = it
      if (it) bookmark(id, name) else debookmark(id)
    }
  )
}