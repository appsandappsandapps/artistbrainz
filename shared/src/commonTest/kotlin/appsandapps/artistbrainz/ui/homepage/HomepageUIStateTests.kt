package appsandapps.artistbrainz.ui.homepage

import androidx.lifecycle.SavedStateHandle
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.test.*
import org.junit.*
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import appsandapps.artistbrainz.ui.homepage.HomepageUIValues
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first

@RunWith(MockitoJUnitRunner.Silent::class)
@ExperimentalCoroutinesApi
class HomepageUIStateTests {

  @Mock lateinit var savedState: SavedStateHandle
  @Mock lateinit var viewModel: appsandapps.artistbrainz.ui.homepage.HomepageViewModel

  @Test fun `uistate sets bookmark num`() = runTest {
    val numBookmarks = 10
    val uiState = appsandapps.artistbrainz.ui.homepage.HomepageUIModel(viewModel)
    uiState
      .update(appsandapps.artistbrainz.ui.homepage.HomepageAction.Bookmarks(numBookmarks))
    val bookMarksNum = uiState.stateFlow.first().bookmarked

    Assert.assertEquals(numBookmarks, bookMarksNum)
  }

}
