package com.example.swapcard.ui.homepage

import androidx.lifecycle.SavedStateHandle
import com.example.swapcard.Application
import com.example.swapcard.repositories.ArtistsRepository
import com.example.swapcard.getByHashCode
import com.example.swapcard.setByHashCode
import com.example.swapcard.utils.DispatchedViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect

class HomepageViewModel(
  application: Application,
  private val savedState: SavedStateHandle,
  private val repository: ArtistsRepository = application.artistsRepository,
  private var mockUiState: HomepageUIState ? = null,
  dispatcher: CoroutineDispatcher = Dispatchers.IO,
): DispatchedViewModel(dispatcher) {

  val uiState = mockUiState ?:
    HomepageUIState(
      viewModel = this,
      existing = savedState.getByHashCode(HomepageUIState.UIValues()),
      saveToParcel = { savedState.setByHashCode(it) }
    )

  init {
    observeArtists()
  }

  private fun observeArtists() = dispatchedLaunch {
    repository.bookmarks.collect {
      var bookmarked = it.bookmarks.size
      uiState.setBookmarked(bookmarked)
    }
  }

}
