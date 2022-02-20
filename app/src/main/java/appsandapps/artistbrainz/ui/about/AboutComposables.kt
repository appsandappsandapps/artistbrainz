package appsandapps.artistbrainz.ui.about

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.datastore.preferences.core.edit
import appsandapps.artistbrainz.R
import appsandapps.artistbrainz.data.Bookmark
import appsandapps.artistbrainz.utils.PREF_KEY_COMPOSE_TOGGLE
import appsandapps.artistbrainz.utils.StateSaver
import appsandapps.artistbrainz.utils.ext.viewModelWithSavedState
import appsandapps.artistbrainz.utils.prefsDataStore
import appsandapps.artistbrainz.ui.bookmarks.BookmarksUIState.Action.*
import appsandapps.artistbrainz.ui.theme.StandardPadding
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * - Shows about text
 * - Switch between Compose / Fragment views system
 */
@Composable
fun AboutScreen() {
  AboutContent()
}

@Composable
private fun AboutContent() {
  Column(
    Modifier.padding(StandardPadding)
  ) {
    Text(
      stringResource(R.string.about_text),
      Modifier.padding(top = StandardPadding),
    )
    Divider(
      Modifier.padding(top = StandardPadding + StandardPadding),
      color = MaterialTheme.colors.onBackground
    )
    Row(
      verticalAlignment = Alignment.CenterVertically,
    ) {
      Text("Use Jetpack Compose?")
      ComposeToggle()
    }
  }
}

/**
 * Switch between compose and fragment view system
 */
@Composable
private fun ComposeToggle() {
  val prefsData = LocalContext.current.prefsDataStore
  val coroutineScope = rememberCoroutineScope()
  var switchState by remember { mutableStateOf(true) }
  val switched: (Boolean) -> Unit = { b: Boolean ->
    switchState = b
    coroutineScope.launch {
      delay(400)
      if(!switchState) switchState = true
      prefsData.edit { settings ->
        settings[PREF_KEY_COMPOSE_TOGGLE] = b
      }
    }
  }
  Switch(
    switchState,
    switched,
  )
}
