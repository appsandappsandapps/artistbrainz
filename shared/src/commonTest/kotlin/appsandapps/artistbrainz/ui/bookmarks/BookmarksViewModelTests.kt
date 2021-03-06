package appsandapps.artistbrainz.ui.bookmarks

import androidx.lifecycle.SavedStateHandle
import appsandapps.artistbrainz.data.Artist
import appsandapps.artistbrainz.data.Bookmarks
import appsandapps.artistbrainz.repositories.ArtistsRepository
import appsandapps.artistbrainz.utils.StateSaver
import appsandapps.artistbrainz.utils.launchAndWait
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.*
import org.junit.*
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner.Silent::class)
@ExperimentalCoroutinesApi
class BookmarksViewModelTests {

  @Before fun setUp() = Dispatchers.setMain(StandardTestDispatcher())
  @After fun tearDown() = Dispatchers.resetMain()

  @Mock lateinit var savedState: SavedStateHandle
  @Mock lateinit var repo: ArtistsRepository
  @Mock lateinit var uiState: appsandapps.artistbrainz.ui.bookmarks.BookmarksUIModel

  @Test fun `viewmodel calls repo bookmarks on init`() = runTest {
    `when`(repo.bookmarks).thenReturn(MutableStateFlow(Bookmarks()))
    launchAndWait {
      appsandapps.artistbrainz.ui.bookmarks.BookmarksViewModel(
        StateSaver(savedState), {}, repo,
        uiState, Dispatchers.Main
      )
    }

    verify(repo, times(1)).bookmarks
  }

  @Test fun `viewmodel calls repo debookmark`() = runTest {
    val artistId = "1"
    `when`(repo.artist).thenReturn(MutableStateFlow(Artist()))
    launchAndWait {
      val vm = appsandapps.artistbrainz.ui.bookmarks.BookmarksViewModel(
        StateSaver(savedState), {}, repo,
        uiState, Dispatchers.Main
      )
      vm.debookmark(artistId)
    }

    verify(repo, times(1)).debookmark(artistId)
  }

}
