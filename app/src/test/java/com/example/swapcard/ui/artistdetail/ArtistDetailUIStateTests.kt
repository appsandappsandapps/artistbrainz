package com.example.swapcard.ui.artistdetail

import androidx.lifecycle.SavedStateHandle
import com.example.swapcard.Application
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
      id = artistId,
      name = artistName
    )
    ArtistDetailUIState(viewModel, uiValues).bookmark()

    verify(viewModel, times(1)).bookmark(artistId, artistName)
  }

  @Test fun `uistate loading false initially`() = runTest {
    val uiState = ArtistDetailUIState(viewModel)
    val state = uiState.valuesFlow.first()

    Assert.assertFalse(state.loading)
  }

  @Test fun `uistate sets loading`() = runTest {
    val uiState = ArtistDetailUIState(viewModel)
    uiState.setLoading(true)
    val state = uiState.valuesFlow.first()

    Assert.assertTrue(state.loading)
  }

}
