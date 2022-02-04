package com.example.architecturetest.ui.textlist

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.architecturetest.Application
import com.example.architecturetest.repositories.TextRepository
import com.example.architecturetest.getByHashCode
import com.example.architecturetest.setByHashCode
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class TextListViewModel(
  private val application: Application,
  private val savedState: SavedStateHandle,
  private val newScreen: () -> Unit,
  private val repository: TextRepository = application.textRepository,
): ViewModel() {

  val uiState =
    TextListUIState(
      viewModel = this,
      existing = savedState.getByHashCode(TextListUIState.Values()),
      saveToParcel = { savedState.setByHashCode(it) }
    )

  init {
    viewModelScope.launch {
      observeNewText()
    }
  }

  private suspend fun observeNewText() {
    repository.refresh()
    repository.allText.collect {
      uiState.setItems(it)
    }
  }

  fun gotoNewScreen() {
    newScreen()
  }


}
