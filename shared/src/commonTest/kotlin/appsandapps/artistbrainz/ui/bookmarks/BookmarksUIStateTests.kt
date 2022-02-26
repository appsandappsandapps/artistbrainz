package appsandapps.artistbrainz.ui.bookmarks

import androidx.lifecycle.SavedStateHandle
import appsandapps.artistbrainz.data.Bookmark
import kotlinx.coroutines.*
import kotlinx.coroutines.test.*
import org.junit.*
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnitRunner
import kotlinx.coroutines.flow.first

@RunWith(MockitoJUnitRunner::class)
@ExperimentalCoroutinesApi
class BookmarksUIStateTests {

  @Mock lateinit var savedState: SavedStateHandle
  @Mock lateinit var viewModel: appsandapps.artistbrainz.ui.bookmarks.BookmarksViewModel

  @Test fun `uistate calls viewmodel debookmark`() = runTest {
    val artistId = "1"
    appsandapps.artistbrainz.ui.bookmarks.BookmarksUIModel(viewModel)
      .update(appsandapps.artistbrainz.ui.bookmarks.BookmarksAction.Debookmark(artistId))
    verify(viewModel, times(1)).debookmark(artistId)
  }

  @Test fun `uistate sets bookmarks flow`() = runTest {
    val uiValues = appsandapps.artistbrainz.ui.bookmarks.BookmarksUIValues()
    val bookmarks = listOf(Bookmark())
    val uiState = appsandapps.artistbrainz.ui.bookmarks.BookmarksUIModel(viewModel, uiValues)
    uiState
      .update(appsandapps.artistbrainz.ui.bookmarks.BookmarksAction.SetBookmarks(bookmarks))
    val state = uiState.stateFlow.first().bookmarks

    Assert.assertEquals(state, bookmarks)
  }

}
