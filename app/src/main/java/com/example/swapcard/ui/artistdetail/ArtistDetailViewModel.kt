package com.example.swapcard.ui.artistdetail

import androidx.lifecycle.SavedStateHandle
import com.example.swapcard.Application
import com.example.swapcard.repositories.ArtistsRepository
import com.example.swapcard.getByHashCode
import com.example.swapcard.setByHashCode
import com.example.swapcard.utils.DispatchedViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect

class ArtistDetailViewModel(
  application: Application,
  savedState: SavedStateHandle,
  private val artistId: String,
  private val repository: ArtistsRepository = application.artistsRepository,
  private var mockUiState: ArtistDetailUIState? = null,
  dispatcher: CoroutineDispatcher = Dispatchers.IO,
): DispatchedViewModel(dispatcher) {

  val uiState = mockUiState ?:
    ArtistDetailUIState(
      viewModel = this,
      existing = savedState.getByHashCode(ArtistDetailUIState.UIValues()),
      saveToParcel = { savedState.setByHashCode(it) }
    )

  init {
    observeArtists()
  }

  private fun observeArtists() = dispatchedLaunch {
    uiState.setLoading(true)
    repository.artist(artistId)
    repository.artist.collect {
      uiState.setLoading(false)
      if(it.error.isNotBlank()) {
        uiState.setError(it.error)
      } else {
        var artist = it
        uiState.setArtist(ArtistDetailUIState.UIValues(
          id = artist.id,
          name = artist.name,
          bookmarked = artist.bookmarked,
          disambiguation = artist.disambiguation,
          rating = artist.rating.value,
          voteCount = artist.rating.voteCount,
        ))
      }
    }
  }

  public fun bookmark(id: String, name: String) = dispatchedLaunch {
    uiState.setLoading(false)
    repository.bookmark(id, name)
  }

  public fun debookmark() = dispatchedLaunch {
    repository.debookmark(artistId)
  }

}
