package appsandapps.artistbrainz.ui.homepage

import androidx.lifecycle.SavedStateHandle
import appsandapps.artistbrainz.Application
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.*
import org.junit.*
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner.Silent::class)
@ExperimentalCoroutinesApi
class HomepageUIStateTests {

  @Mock lateinit var app: Application
  @Mock lateinit var savedState: SavedStateHandle
  @Mock lateinit var viewModel: HomepageViewModel

  @Test fun `uistate sets bookmark num`() = runTest {
    val numBookmarks = 10
    val uiState = HomepageUIState(viewModel)
    uiState.setBookmarked(numBookmarks)
    val bookMarksNum = uiState.stateFlow.first().bookmarked

    Assert.assertEquals(numBookmarks, bookMarksNum)
  }

}
