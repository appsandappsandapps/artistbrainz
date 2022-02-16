package appsandapps.artistbrainz.ui.artistdetail

import androidx.lifecycle.SavedStateHandle
import appsandapps.artistbrainz.Application
import appsandapps.artistbrainz.data.Artist
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.*
import org.junit.*
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
@ExperimentalCoroutinesApi
class ArtistDetailUIStateTests {

  @Mock lateinit var app: Application
  @Mock lateinit var savedState: SavedStateHandle
  @Mock lateinit var viewModel: ArtistDetailViewModel

  @Test fun `uistate calls viewmodel bookmark`() = runTest {
    val artistId = "1"
    val artistName = "2"
    val uiValues = ArtistDetailUIState.UIValues(
      artist = Artist(artistId, artistName)
    )
    ArtistDetailUIState(viewModel, uiValues)
      .update(ArtistDetailUIState.Action.Bookmark())

    verify(viewModel, times(1)).bookmark(artistId, artistName)
  }

  @Test fun `uistate sets loading on init`() = runTest {
    val uiState = ArtistDetailUIState(viewModel)
    val state = uiState.stateFlow.first()

    Assert.assertTrue(state.loading)
  }

}
