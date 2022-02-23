package appsandapps.artistbrainz.ui.bookmarks

import androidx.lifecycle.SavedStateHandle
import appsandapps.artistbrainz.Application
import appsandapps.artistbrainz.data.Bookmark
import kotlinx.coroutines.*
import kotlinx.coroutines.test.*
import org.junit.*
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
@ExperimentalCoroutinesApi
class BookmarksUIStateTests {

  @Mock lateinit var app: Application
  @Mock lateinit var savedState: SavedStateHandle
  @Mock lateinit var viewModel: appsandapps.artistbrainz.ui.bookmarks.BookmarksViewModel

  @Test fun `uistate calls viewmodel debookmark`() = runTest {
    val artistId = "1"
    appsandapps.artistbrainz.ui.bookmarks.BookmarksUIState(viewModel)
      .update(appsandapps.artistbrainz.ui.bookmarks.BookmarksUIState.Action.Debookmark(artistId))
    verify(viewModel, times(1)).debookmark(artistId)
  }

  @Test fun `uistate sets bookmarks flow`() = runTest {
    val uiValues = appsandapps.artistbrainz.ui.bookmarks.BookmarksUIState.UIValues()
    val bookmarks = listOf(Bookmark())
    val uiState = appsandapps.artistbrainz.ui.bookmarks.BookmarksUIState(viewModel, uiValues)
    uiState
      .update(appsandapps.artistbrainz.ui.bookmarks.BookmarksUIState.Action.SetBookmarks(bookmarks))
    val state = uiState.stateFlow.first().bookmarks

    Assert.assertEquals(state, bookmarks)
  }

}
