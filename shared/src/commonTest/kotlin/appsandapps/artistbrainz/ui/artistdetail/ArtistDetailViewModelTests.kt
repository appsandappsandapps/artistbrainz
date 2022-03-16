package appsandapps.artistbrainz.ui.artistdetail

import androidx.lifecycle.SavedStateHandle
import appsandapps.artistbrainz.data.Artist
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
import org.mockito.Mockito.validateMockitoUsage
import kotlinx.coroutines.flow.*


@RunWith(MockitoJUnitRunner::class)
@ExperimentalCoroutinesApi
class ArtistDetailViewModelTests {

  @Before fun setUp() = Dispatchers.setMain(StandardTestDispatcher())
  @After fun tearDown() = Dispatchers.resetMain()
  @After fun validate() {
    validateMockitoUsage()
  }
  @Mock lateinit var savedState: SavedStateHandle
  @Mock lateinit var repo: ArtistsRepository
  @Mock lateinit var uiState: appsandapps.artistbrainz.ui.artistdetail.ArtistDetailUIModel

  @Test fun `viewmodel calls repo artist on init`() = runTest {
    val artistId = "1"
    `when`(repo.artist).thenReturn(MutableStateFlow(Artist()))
    launchAndWait {
      appsandapps.artistbrainz.ui.artistdetail.ArtistDetailViewModel(
        StateSaver(savedState), artistId, {}, repo,
        uiState, Dispatchers.Main
      )
    }

    verify(repo, times(1)).artist(artistId)
  }

  @Test fun `viewmodel calls ui set artist on init`() = runTest {
    val artistId = "b"
    val artistName = "b"
    val artist = Artist(artistId, artistName)
    `when`(repo.artist).thenReturn(MutableStateFlow(artist))
    var foundArtist: Artist? = null
    launchAndWait {
      val vm = appsandapps.artistbrainz.ui.artistdetail.ArtistDetailViewModel(
        StateSaver(savedState), "", {}, repo,
        null, Dispatchers.Main
      )
      foundArtist = vm.uiModel.stateFlow.take(2).last().artist
    }

    Assert.assertEquals(artist, foundArtist)
  }

}
