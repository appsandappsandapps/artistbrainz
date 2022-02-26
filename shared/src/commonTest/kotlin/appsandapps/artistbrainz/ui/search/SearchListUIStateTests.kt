package appsandapps.artistbrainz.ui.search

import androidx.lifecycle.SavedStateHandle
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.*
import org.junit.*
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
@ExperimentalCoroutinesApi
class SearchListUIStateTests {

  @Mock lateinit var savedState: SavedStateHandle
  @Mock lateinit var viewModel: appsandapps.artistbrainz.ui.search.SearchListViewModel

  @Test fun `uistate calls viewmodel bookmark`() = runTest {
    val artistId = "1"
    val artistName = "2"
    appsandapps.artistbrainz.ui.search.SearchListUIModel(viewModel)
      .update(appsandapps.artistbrainz.ui.search.SearchAction.Bookmark(artistId, artistName))

    verify(viewModel, times(1)).bookmark(artistId, artistName)
  }

  @Test fun `uistate loading false initially`() = runTest {
    val uiState = appsandapps.artistbrainz.ui.search.SearchListUIModel(viewModel)
    val loading = uiState.stateFlow.first().loading

    Assert.assertFalse(loading)
  }

  @Test fun `uistate sets loading on search`() = runTest {
    val uiState = appsandapps.artistbrainz.ui.search.SearchListUIModel(viewModel)
    uiState.update(appsandapps.artistbrainz.ui.search.SearchAction.PressSearch())
    //val loading = (uiState.stateFlow as StateFlow<SearchUIValues>).first().loading
    val loading = uiState.stateFlow.first().loading

    Assert.assertTrue(loading)
  }

}
