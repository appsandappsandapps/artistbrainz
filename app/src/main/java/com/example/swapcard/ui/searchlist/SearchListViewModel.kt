package com.example.swapcard.ui.searchlist

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

class SearchListViewModel(
  private val application: Application,
  private val savedState: SavedStateHandle,
  private val gotoDetail: (Int) -> Unit,
  private val repository: MusicRepository = application.musicRepository,
): ViewModel() {

  val uiState =
    SearchListUIState(
      viewModel = this,
      existing = savedState.getByHashCode(SearchListUIState.Values()),
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
      uiState.setItems(it.artists.map {
        SearchListUIState.ArtistUI(it.id, it.name, it.bookmarked)
      })
    }
  }

  public fun paginateSearch() = viewModelScope.launch {
    repository.paginateLastSearch()
  }

  public fun bookmark(id: Int) = viewModelScope.launch {
    repository.bookmark(id)
  }

  public fun debookmark(id: Int) = viewModelScope.launch {
    repository.debookmark(id)
  }

  public fun gotoArtistDetail(id: Int) {
    gotoDetail(id)
  }

}
