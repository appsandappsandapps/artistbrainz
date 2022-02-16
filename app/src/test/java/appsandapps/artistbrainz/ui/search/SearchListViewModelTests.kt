package appsandapps.artistbrainz.ui.search

import androidx.lifecycle.SavedStateHandle
import appsandapps.artistbrainz.Application
import appsandapps.artistbrainz.data.Artists
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

@RunWith(MockitoJUnitRunner::class)
@ExperimentalCoroutinesApi
class SearchListViewModelTests {

  @Before fun setUp() = Dispatchers.setMain(StandardTestDispatcher())
  @After fun tearDown() = Dispatchers.resetMain()

  @Mock lateinit var app: Application
  @Mock lateinit var savedState: SavedStateHandle
  @Mock lateinit var repo: ArtistsRepository
  @Mock lateinit var uiState: SearchListUIState

  @Test fun `viewmodel calls repo artist on init`() = runTest {
    `when`(repo.searchedForArtists).thenReturn(MutableStateFlow(Artists()))
    `when`(repo.bookmarks).thenReturn(MutableStateFlow(Bookmarks()))
    launchAndWait {
      SearchListViewModel(app, StateSaver(savedState), {}, repo,
        uiState, Dispatchers.Main)
    }

    verify(repo, times(1)).searchedForArtists
  }

  @Test fun `viewmodel calls repo bookmarks on init`() = runTest {
    `when`(repo.searchedForArtists).thenReturn(MutableStateFlow(Artists()))
    `when`(repo.bookmarks).thenReturn(MutableStateFlow(Bookmarks()))
    launchAndWait {
      SearchListViewModel(app, StateSaver(savedState), {}, repo,
        uiState, Dispatchers.Main)
    }

    verify(repo, times(1)).bookmarks
  }

  @Test fun `viewmodel calls repo search`() = runTest {
    val search = "hello"
    `when`(repo.searchedForArtists).thenReturn(MutableStateFlow(Artists()))
    `when`(repo.bookmarks).thenReturn(MutableStateFlow(Bookmarks()))
    launchAndWait {
      val vm = SearchListViewModel(app, StateSaver(savedState), {}, repo,
        uiState, Dispatchers.Main)
      vm.searchArtists(search)
    }

    verify(repo, times(1)).search(search)
  }

}
