package com.example.swapcard.ui.dashboard

import android.util.Log
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
      existing = savedState.getByHashCode(HomepageUIState.Values()),
      saveToParcel = { savedState.setByHashCode(it) }
    )

  init {
    viewModelScope.launch {
      observeArtists()
    }
  }

  private suspend fun observeArtists() {
    repository.refresh()
    repository.searchedForArtists.collect {
      var bookmarked = it.artists.filter { it.bookmarked }.size
      uiState.setBookmarked(bookmarked)
    }
  }

}
