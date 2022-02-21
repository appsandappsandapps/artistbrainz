package appsandapps.artistbrainz.ui.homepage

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import appsandapps.artistbrainz.ui.about.AboutScreen
import appsandapps.artistbrainz.ui.bookmarks.BookmarksScreen
import appsandapps.artistbrainz.ui.search.SearchListScreen
import appsandapps.artistbrainz.ui.theme.StandardPadding
import appsandapps.artistbrainz.utils.StateSaver
import appsandapps.artistbrainz.utils.ext.viewModelWithSavedState
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.PagerState
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.launch

/**
 * - Starts the view pager
 * - Shows the app tab bar
 * - Closes keyboard on tab change
 * - Updates bookmarks tab with bookmarks num
 */
@Composable
fun HomepageScreen() {
  // View model and UI State
  val stateObj = viewModelWithSavedState {
    HomepageViewModel(
      StateSaver(it),
    )
  }.uiState
  val uiValues = stateObj.stateFlow.collectAsState().value

  HomepageContent(
    uiValues.bookmarked
  )
}

@OptIn(ExperimentalPagerApi::class)
@Composable
private fun HomepageContent(
  bookmarksNum: Int,
) {
  val items = listOf<@Composable () -> Unit>(
    { SearchListScreen() },
    { BookmarksScreen() },
    { AboutScreen() },
  )
  val pageState = rememberPagerState()
  Column {
    AppTabRow(pageState, bookmarksNum)
    HorizontalPager(
      items.size,
      Modifier.fillMaxSize(),
      state = pageState,
      verticalAlignment = Alignment.Top,
    ) {
      items[it]()
    }
  }
}

/**
 * - Shows all the tabs
 * - Gives "Bookmarks" a badge
 */
@OptIn(ExperimentalPagerApi::class)
@Composable
private fun AppTabRow(
  pageState: PagerState,
  bookmarksNum: Int
) {
  TabRow(
    pageState.currentPage,
    Modifier.fillMaxWidth(),
    indicator = {
      Box(
        Modifier
          .tabIndicatorOffset(it[pageState.currentPage])
          .height(4.dp)
          .border(4.dp, MaterialTheme.colors.onPrimary)
      )
    }
  ) {
    BadgedViewpagerTab("Artists", pageState, 0, -1)
    BadgedViewpagerTab("Bookmarks", pageState, 1, bookmarksNum)
    BadgedViewpagerTab("About", pageState, 2, -1)
  }
}

/**
 * - Scrolls the ViewPager via PagerState
 * to this tab when clicked
 * - Optionally gives a badge to the tab
 * - Closes keyboard when tab clicked
 */
@OptIn(ExperimentalPagerApi::class, ExperimentalComposeUiApi::class)
@Composable
private fun BadgedViewpagerTab(
  name: String,
  pageState: PagerState,
  tabNum: Int,
  badgeNum: Int,
) {
  val kbController = LocalSoftwareKeyboardController.current
  val scope = rememberCoroutineScope()
  val onTabClick: () -> Unit = {
    scope.launch {
      pageState.scrollToPage(tabNum)
      kbController?.hide()
    }
  }
  Tab(
    pageState.currentPage == tabNum,
    onTabClick,
    Modifier.padding(6.dp, StandardPadding + StandardPadding),
    selectedContentColor = MaterialTheme.colors.onPrimary,
    unselectedContentColor = MaterialTheme.colors.onPrimary,
  ) {
    if(badgeNum > 0)
      BadgedBox({ TabBadge(badgeNum) }) {
        TabText(name)
      }
    else
      TabText(name)
  }
}

@Composable
private fun TabBadge(badgeNum: Int) {
  Badge(
    Modifier.padding(start = 4.dp),
    backgroundColor = MaterialTheme.colors.onPrimary,
    contentColor = MaterialTheme.colors.primary,
  ) {
    Text(
      "$badgeNum",
      fontSize = 12.sp,
    )
  }
}

@Composable
private fun TabText(name: String) {
  Text(
    name.uppercase(),
    fontSize = 14.sp,
  )
}
