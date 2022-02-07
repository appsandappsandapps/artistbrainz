package com.example.swapcard.ui.bookmarks

import androidx.lifecycle.SavedStateHandle
import com.example.swapcard.Application
import com.example.swapcard.repositories.ArtistsRepository
import com.example.swapcard.getByHashCode
import com.example.swapcard.setByHashCode
import com.example.swapcard.utils.DispatchedViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect

class BookmarksViewModel(
  application: Application,
  private val savedState: SavedStateHandle,
  private val gotoDetail: (String) -> Unit,
  private val repository: ArtistsRepository = application.artistsRepository,
  private var mockUiState: BookmarksUIState? = null,
  dispatcher: CoroutineDispatcher = Dispatchers.IO,
): DispatchedViewModel(dispatcher) {

  val uiState = mockUiState ?:
    BookmarksUIState(
      viewModel = this,
      existing = savedState.getByHashCode(BookmarksUIState.UIValues()),
      saveToParcel = { savedState.setByHashCode(it) }
    )

  init {
    observeArtists()
  }

  private fun observeArtists() = dispatchedLaunch {
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

  public fun debookmark(id: String) = dispatchedLaunch {
    repository.debookmark(id)
  }

  public fun gotoDetailScreen(id: String) {
    gotoDetail(id)
  }

}
