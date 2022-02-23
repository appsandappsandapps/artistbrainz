package appsandapps.artistbrainz.ui.search

import androidx.lifecycle.SavedStateHandle
import appsandapps.artistbrainz.Application
import kotlinx.coroutines.*
import kotlinx.coroutines.test.*
import org.junit.*
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
@ExperimentalCoroutinesApi
class SearchListUIStateTests {

  @Mock lateinit var app: Application
  @Mock lateinit var savedState: SavedStateHandle
  @Mock lateinit var viewModel: appsandapps.artistbrainz.ui.search.SearchListViewModel

  @Test fun `uistate calls viewmodel bookmark`() = runTest {
    val artistId = "1"
    val artistName = "2"
    appsandapps.artistbrainz.ui.search.SearchListUIState(viewModel)
      .update(appsandapps.artistbrainz.ui.search.SearchListUIState.Action.Bookmark(artistId, artistName))

    verify(viewModel, times(1)).bookmark(artistId, artistName)
  }

  @Test fun `uistate loading false initially`() = runTest {
    val uiState = appsandapps.artistbrainz.ui.search.SearchListUIState(viewModel)
    val loading = uiState.stateFlow.first().loading

    Assert.assertFalse(loading)
  }

  @Test fun `uistate sets loading on search`() = runTest {
    val uiState = appsandapps.artistbrainz.ui.search.SearchListUIState(viewModel)
    uiState.update(appsandapps.artistbrainz.ui.search.SearchListUIState.Action.PressSearch())
    val loading = uiState.stateFlow.first().loading

    Assert.assertTrue(loading)
  }

}
