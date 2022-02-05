package com.example.swapcard.ui.search

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
  private val gotoDetail: (String) -> Unit,
  private val repository: MusicRepository = application.musicRepository,
): ViewModel() {

  val uiState =
    SearchListUIState(
      viewModel = this,
      existing = savedState.getByHashCode(SearchListUIState.UIValues()),
      saveToParcel = { savedState.setByHashCode(it) }
    )

  init {
    viewModelScope.launch { observeArtists() }
    viewModelScope.launch { observeBookmarks() }
  }

  private suspend fun observeArtists() {
    repository.searchedForArtists.collect {
      if(it.error.isNotBlank()) {
        uiState.setError(it.error)
      } else if(!it.paginated && it.artists.size == 0) {
        uiState.setEmptyList(true)
      } else
        uiState.addArtists(it.artists.map {
          SearchListUIState.ArtistUI(it.id, it.name, it.bookmarked)
        })
    }
  }

  private suspend fun observeBookmarks() {
    repository.bookmarks.collect {
      val ids = it.bookmarks.map { it.id }
      uiState.applyBookmarks(ids)
    }
  }

  public fun paginateSearch() = viewModelScope.launch {
    repository.paginateLastSearch()
  }

  public fun bookmark(id: String, name: String) = viewModelScope.launch {
    repository.bookmark(id, name)
  }

  public fun debookmark(id: String) = viewModelScope.launch {
    repository.debookmark(id)
  }

  public fun gotoArtistDetail(id: String) {
    gotoDetail(id)
  }

  public fun searchArtists(s:String) = viewModelScope.launch {
    uiState.setLoading(true)
    repository.search(s)
  }

}
