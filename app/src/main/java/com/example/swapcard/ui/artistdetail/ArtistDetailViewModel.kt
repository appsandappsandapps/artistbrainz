package com.example.swapcard.ui.artistdetail

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

class ArtistDetailViewModel(
  private val application: Application,
  private val savedState: SavedStateHandle,
  private val artistId: Int,
  private val repository: MusicRepository = application.musicRepository,
): ViewModel() {

  val uiState =
    ArtistDetailUIState(
      viewModel = this,
      existing = savedState.getByHashCode(ArtistDetailUIState.Values()),
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
      var artist = it.artists.filter { it.id == artistId }[0]
      uiState.setArtist(ArtistDetailUIState.Values(
        id = artist.id,
        name = artist.name,
        bookmarked = artist.bookmarked,
        disambiguation = artist.disambiguation,
        rating = artist.rating.value,
        voteCount = artist.rating.voteCount,
      ))
    }
  }

  public fun bookmark() {
    viewModelScope.launch {
      repository.bookmark(artistId)
    }
  }

  public fun debookmark() {
    viewModelScope.launch {
      repository.debookmark(artistId)
    }
  }

}
