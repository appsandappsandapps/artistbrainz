package com.example.swapcard.ui.bookmarks

import androidx.lifecycle.SavedStateHandle
import com.example.swapcard.Application
import com.example.swapcard.data.Artist
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
class BookmarksViewModelTests {

  @Before fun setUp() = Dispatchers.setMain(StandardTestDispatcher())
  @After fun tearDown() = Dispatchers.resetMain()

  @Mock lateinit var app: Application
  @Mock lateinit var savedState: SavedStateHandle
  @Mock lateinit var repo: ArtistsRepository
  @Mock lateinit var uiState: BookmarksUIState

  @Test fun `viewmodel calls repo bookmarks on init`() = runTest {
    `when`(repo.bookmarks).thenReturn(MutableStateFlow(Bookmarks()))
    launchAndWait {
      BookmarksViewModel(app, savedState, {}, repo,
        uiState, Dispatchers.Main)
    }

    verify(repo, times(1)).bookmarks
  }

  @Test fun `viewmodel calls repo debookmark`() = runTest {
    val artistId = "1"
    `when`(repo.artist).thenReturn(MutableStateFlow(Artist()))
    launchAndWait {
      val vm = BookmarksViewModel(app, savedState, {}, repo,
        uiState, Dispatchers.Main)
      vm.debookmark(artistId)
    }

    verify(repo, times(1)).debookmark(artistId)
  }

}
