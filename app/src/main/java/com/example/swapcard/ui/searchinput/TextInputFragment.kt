package com.example.swapcard.ui.searchinput

import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.swapcard.databinding.TextinputBinding
import com.example.swapcard.hideKeyboard
import com.example.swapcard.viewModelWithSavedState
import com.example.swapcard.R
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class TextInputFragment : Fragment(R.layout.textinput) {

  override fun onViewCreated(view: android.view.View, savedInstanceState: android.os.Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    val binding = TextinputBinding.bind(view)
    val textInputViewModel = viewModelWithSavedState {
      app, savedState ->
      TextInputViewModel(app, savedState)
    }
    listenOnViews(binding, textInputViewModel.uiState)
    listenOnStateChange(binding, textInputViewModel.uiState)
  }

  private fun listenOnStateChange(
    binding: com.example.swapcard.databinding.TextinputBinding,
    uiState: TextInputUIState,
  ) = lifecycleScope.launch {
    uiState.valuesFlow.collect {
      if(it.inputText != binding.edittext.text.toString()) {
        binding.edittext.setText(it.inputText)
      }
    }
  }

  private fun listenOnViews(
    binding: com.example.swapcard.databinding.TextinputBinding,
    uiState: TextInputUIState,
  ) {
    binding.edittext.doAfterTextChanged {
      uiState.setInputText("$it")
    }
    binding.edittext.setOnEditorActionListener { tv, id, event ->
      uiState.pressEnter()
      hideKeyboard()
      true
    }
  }

}