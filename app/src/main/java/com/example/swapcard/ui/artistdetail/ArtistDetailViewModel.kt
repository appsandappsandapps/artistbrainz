package com.example.swapcard.ui.artistdetail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.swapcard.Application
import com.example.swapcard.repositories.MusicRepository
import com.example.swapcard.getByHashCode
import com.example.swapcard.setByHashCode
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class ArtistDetailViewModel(
  private val application: Application,
  private val savedState: SavedStateHandle,
  private val artistId: String,
  private val repository: MusicRepository = application.musicRepository,
): ViewModel() {

  val uiState =
    ArtistDetailUIState(
      viewModel = this,
      existing = savedState.getByHashCode(ArtistDetailUIState.UIValues()),
      saveToParcel = { savedState.setByHashCode(it) }
    )

  init {
    viewModelScope.launch {
      observeArtists()
    }
  }

  private suspend fun observeArtists() {
    repository.artist(artistId)
    repository.artist.collect {
      var artist = it
      uiState.setArtist(ArtistDetailUIState.UIValues(
        id = artist.id,
        name = artist.name,
        bookmarked = repository.isBookmarked(it.id),
        disambiguation = artist.disambiguation,
        //rating = artist.rating.value,
        //voteCount = artist.rating.voteCount,
      ))
    }
  }

  public fun bookmark(id: String, name: String) {
    viewModelScope.launch {
      repository.bookmark(id, name)
    }
  }

  public fun debookmark() {
    viewModelScope.launch {
      repository.debookmark(artistId)
    }
  }

}
