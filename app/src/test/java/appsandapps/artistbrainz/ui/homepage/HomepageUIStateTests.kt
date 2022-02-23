package appsandapps.artistbrainz.ui.homepage

import androidx.lifecycle.SavedStateHandle
import appsandapps.artistbrainz.Application
import kotlinx.coroutines.*
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
  @Mock lateinit var viewModel: appsandapps.artistbrainz.ui.homepage.HomepageViewModel

  @Test fun `uistate sets bookmark num`() = runTest {
    val numBookmarks = 10
    val uiState = appsandapps.artistbrainz.ui.homepage.HomepageUIState(viewModel)
    uiState
      .update(appsandapps.artistbrainz.ui.homepage.HomepageUIState.Action.Bookmarks(numBookmarks))
    val bookMarksNum = uiState.stateFlow.first().bookmarked

    Assert.assertEquals(numBookmarks, bookMarksNum)
  }

}
