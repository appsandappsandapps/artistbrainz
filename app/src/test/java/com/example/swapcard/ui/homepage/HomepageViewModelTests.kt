package com.example.swapcard.ui.homepage

import androidx.lifecycle.SavedStateHandle
import com.example.swapcard.Application
import com.example.swapcard.data.Bookmarks
import com.example.swapcard.repositories.ArtistsRepository
import com.example.swapcard.utils.launchAndWait
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
class HomepageViewModelTests {

  @Before fun setUp() = Dispatchers.setMain(StandardTestDispatcher())
  @After fun tearDown() = Dispatchers.resetMain()

  @Mock lateinit var app: Application
  @Mock lateinit var savedState: SavedStateHandle
  @Mock lateinit var repo: ArtistsRepository
  @Mock lateinit var uiState: HomepageUIState

  @Test fun `viewmodel calls repo bookmarks on init`() = runTest {
    `when`(repo.bookmarks).thenReturn(MutableStateFlow(Bookmarks()))
    launchAndWait {
      HomepageViewModel(app, savedState, repo,
        uiState, Dispatchers.Main)
    }

    verify(repo, times(1)).bookmarks
  }

}
