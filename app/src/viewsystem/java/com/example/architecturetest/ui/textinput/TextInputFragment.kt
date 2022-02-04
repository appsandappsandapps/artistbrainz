package com.example.architecturetest.ui.textinput

import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.lifecycleScope
import com.example.architecturetest.hideKeyboard
import com.example.architecturetest.viewModelWithSavedState
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class TextInputFragment : androidx.fragment.app.Fragment(com.example.architecturetest.R.layout.textinput) {

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
    binding: com.example.architecturetest.databinding.TextinputBinding,
    uiState: TextInputUIState,
  ) = lifecycleScope.launch {
    uiState.valuesFlow.collect {
      if(it.inputText != binding.edittext.text.toString()) {
        binding.edittext.setText(it.inputText)
      }
      binding.checkbox.isChecked = it.yellowBg
      val bg = if(it.yellowBg) android.graphics.Color.YELLOW else android.graphics.Color.LTGRAY
      binding.edittext.setBackgroundColor(bg)
    }
  }

  private fun listenOnViews(
    binding: com.example.architecturetest.databinding.TextinputBinding,
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
    binding.edittext.doAfterTextChanged {
      uiState.setInputText("$it")
    }
    binding.checkbox.setOnCheckedChangeListener { button, b ->
      uiState.setYellowBg(b)
    }
  }

}