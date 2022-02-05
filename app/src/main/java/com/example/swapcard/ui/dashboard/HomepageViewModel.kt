package com.example.swapcard.ui.dashboard

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.swapcard.Application
import com.example.swapcard.repositories.MusicRepository
import com.example.swapcard.getByHashCode
import com.example.swapcard.setByHashCode
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class HomepageViewModel(
  private val application: Application,
  private val savedState: SavedStateHandle,
  private val repository: MusicRepository = application.musicRepository,
): ViewModel() {

  val uiState =
    HomepageUIState(
      viewModel = this,
      existing = savedState.getByHashCode(HomepageUIState.UIValues()),
      saveToParcel = { savedState.setByHashCode(it) }
    )

  init {
    viewModelScope.launch {
      observeArtists()
    }
  }

  private suspend fun observeArtists() {
    repository.bookmarks.collect {
      var bookmarked = it.bookmarks.size
      uiState.setBookmarked(bookmarked)
    }
  }

}
