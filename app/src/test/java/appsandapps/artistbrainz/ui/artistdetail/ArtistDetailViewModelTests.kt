package appsandapps.artistbrainz.ui.artistdetail

import androidx.lifecycle.SavedStateHandle
import appsandapps.artistbrainz.Application
import appsandapps.artistbrainz.data.Artist
import appsandapps.artistbrainz.repositories.ArtistsRepository
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
class ArtistDetailViewModelTests {

  @Before fun setUp() = Dispatchers.setMain(StandardTestDispatcher())
  @After fun tearDown() = Dispatchers.resetMain()

  @Mock lateinit var app: Application
  @Mock lateinit var savedState: SavedStateHandle
  @Mock lateinit var repo: ArtistsRepository
  @Mock lateinit var uiState: ArtistDetailUIState

  @Test fun `viewmodel calls repo artist on init`() = runTest {
    val artistId = "1"
    `when`(repo.artist).thenReturn(MutableStateFlow(Artist()))
    launchAndWait {
      ArtistDetailViewModel(app, savedState, artistId, {}, repo,
        uiState, Dispatchers.Main)
    }

    verify(repo, times(1)).artist(artistId)
  }

  @Test fun `viewmodel calls ui set artist on init`() = runTest {
    val artistId = "b"
    val artistName = "b"
    val artist = Artist(artistId, artistName)
    `when`(repo.artist).thenReturn(MutableStateFlow(artist))
    launchAndWait {
      ArtistDetailViewModel(app, savedState, "", {}, repo,
        uiState, Dispatchers.Main)
    }

    verify(uiState, times(1)).setArtist(artistId, artistName)
  }

}
