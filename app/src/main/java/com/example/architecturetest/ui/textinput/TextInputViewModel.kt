package com.example.architecturetest.ui.textinput

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.architecturetest.Application
import com.example.architecturetest.repositories.TextRepository
import com.example.architecturetest.getByHashCode
import com.example.architecturetest.setByHashCode
import kotlinx.coroutines.launch

class TextInputViewModel(
  private val application: Application,
  private val savedState: SavedStateHandle,
  private val repository: TextRepository = application.textRepository,
): ViewModel() {

  val uiState =
    TextInputUIState(
      viewModel = this,
      existing = savedState.getByHashCode(TextInputUIState.Values()),
      saveToParcel = { savedState.setByHashCode(it) },
    )

  fun addText(s:String) {
    viewModelScope.launch {
      repository.addText(s)
    }
  }

}
