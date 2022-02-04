package com.example.swapcard.ui.searchinput

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.swapcard.Application
import com.example.swapcard.repositories.MusicRepository
import com.example.swapcard.getByHashCode
import com.example.swapcard.setByHashCode
import kotlinx.coroutines.launch

class TextInputViewModel(
  private val application: Application,
  private val savedState: SavedStateHandle,
  private val repository: MusicRepository = application.musicRepository,
): ViewModel() {

  val uiState =
    TextInputUIState(
      viewModel = this,
      existing = savedState.getByHashCode(TextInputUIState.Values()),
      saveToParcel = { savedState.setByHashCode(it) },
    )

  fun addText(s:String) {
    viewModelScope.launch {
      repository.search(s,)
    }
  }

}
