package com.example.swapcard.ui.artistdetail

import android.util.Log
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
    observeArtist()
  }

  private fun observeArtist() = dispatchedLaunch {
    repository.artist(artistId)
    repository.artist.collect {
      if(it.error.isNotBlank()) {
        uiState.setError(it.error)
      } else {
        var artist = it
        uiState.setArtist(
          artist.id,
          artist.name,
          artist.bookmarked,
          artist.disambiguation,
          artist.rating.value,
          artist.rating.voteCount,
        )
      }
    }
  }

  public fun bookmark(id: String, name: String) = dispatchedLaunch {
    repository.bookmark(id, name)
  }

  public fun debookmark() = dispatchedLaunch {
    repository.debookmark(artistId)
  }

}
