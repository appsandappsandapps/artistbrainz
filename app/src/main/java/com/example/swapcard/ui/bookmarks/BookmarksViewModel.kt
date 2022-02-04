package com.example.swapcard.ui.bookmarks

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.swapcard.Application
import com.example.swapcard.repositories.MusicRepository
import com.example.swapcard.getByHashCode
import com.example.swapcard.setByHashCode
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class BookmarksViewModel(
  private val application: Application,
  private val savedState: SavedStateHandle,
  private val repository: MusicRepository = application.musicRepository,
): ViewModel() {

  val uiState =
    BookmarksUIState(
      viewModel = this,
      existing = savedState.getByHashCode(BookmarksUIState.Values()),
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
      val bookmarks = it.artists.filter { it.bookmarked }
        .map {
          BookmarksUIState.BookmarkUI(it.id, it.name)
        }
      uiState.setItems(BookmarksUIState.Values(bookmarks))
    }
  }

  public fun debookmark(id: String) {
    viewModelScope.launch {
      repository.debookmark(id)
    }
  }

}
