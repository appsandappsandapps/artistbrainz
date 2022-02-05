package com.example.swapcard.ui.bookmarks

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.swapcard.Application
import com.example.swapcard.repositories.MusicRepository
import com.example.swapcard.getByHashCode
import com.example.swapcard.repositories.Bookmarks
import com.example.swapcard.setByHashCode
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class BookmarksViewModel(
  private val application: Application,
  private val savedState: SavedStateHandle,
  private val gotoDetail: (String) -> Unit,
  private val repository: MusicRepository = application.musicRepository,
): ViewModel() {

  val uiState =
    BookmarksUIState(
      viewModel = this,
      existing = savedState.getByHashCode(BookmarksUIState.UIValues()),
      saveToParcel = { savedState.setByHashCode(it) }
    )

  init {
    viewModelScope.launch {
      observeArtists()
    }
  }

  private suspend fun observeArtists() {
    repository.bookmarks.collect {
      val bookmarks = it.bookmarks.map {
        BookmarksUIState.BookmarkUI(
          it.id,
          it.name,
        )
      }
      uiState.setBookmarks(BookmarksUIState.UIValues(bookmarks))
    }
  }

  public fun debookmark(id: String) {
    viewModelScope.launch {
      repository.debookmark(id)
    }
  }

  public fun gotoDetailScreen(id: String) {
    gotoDetail(id)
  }

}
