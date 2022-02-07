package com.example.swapcard.ui.search

import androidx.lifecycle.SavedStateHandle
import com.example.swapcard.Application
import com.example.swapcard.data.Artists
import com.example.swapcard.data.Bookmarks
import com.example.swapcard.repositories.ArtistsRepository
import com.example.swapcard.utils.launchAndWait
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.*
import org.junit.*
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
@ExperimentalCoroutinesApi
class SearchListViewModelTests {

  @Before fun setUp() = Dispatchers.setMain(StandardTestDispatcher())
  @After fun tearDown() = Dispatchers.resetMain()

  @Mock lateinit var app: Application
  @Mock lateinit var savedState: SavedStateHandle
  @Mock lateinit var repo: ArtistsRepository
  @Mock lateinit var uiState: SearchListUIState

  @Test fun `viewmodel calls repo artist on init`() = runTest {
    `when`(repo.searchedForArtists).thenReturn(MutableStateFlow(Artists()))
    `when`(repo.bookmarks).thenReturn(MutableStateFlow(Bookmarks()))
    launchAndWait {
      SearchListViewModel(app, savedState, {}, repo,
        uiState, Dispatchers.Main)
    }

    verify(repo, times(1)).searchedForArtists
  }

  @Test fun `viewmodel calls repo bookmarks on init`() = runTest {
    `when`(repo.searchedForArtists).thenReturn(MutableStateFlow(Artists()))
    `when`(repo.bookmarks).thenReturn(MutableStateFlow(Bookmarks()))
    launchAndWait {
      SearchListViewModel(app, savedState, {}, repo,
        uiState, Dispatchers.Main)
    }

    verify(repo, times(1)).bookmarks
  }

  @Test fun `viewmodel calls repo search`() = runTest {
    val search = "hello"
    `when`(repo.searchedForArtists).thenReturn(MutableStateFlow(Artists()))
    `when`(repo.bookmarks).thenReturn(MutableStateFlow(Bookmarks()))
    launchAndWait {
      val vm = SearchListViewModel(app, savedState, {}, repo,
        uiState, Dispatchers.Main)
      vm.searchArtists(search)
    }

    verify(repo, times(1)).search(search)
  }

}
