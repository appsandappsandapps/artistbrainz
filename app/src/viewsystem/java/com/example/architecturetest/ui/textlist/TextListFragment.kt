package com.example.architecturetest.ui.textlist

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.architecturetest.R
import com.example.architecturetest.databinding.TextlistBinding
import com.example.architecturetest.viewModelWithSavedState
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class TextListFragment : Fragment(R.layout.textlist) {

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    val binding = TextlistBinding.bind(view)

    val textListViewModel = viewModelWithSavedState {
      app, savedState -> TextListViewModel(app, savedState, {
        gotoAboutFragment()
      })
    }

    listenOnViews(binding, textListViewModel.uiState)
    listenOnStateChange(binding, textListViewModel.uiState)
  }

  private fun listenOnViews(
    binding: TextlistBinding,
    uiState: TextListUIState,
  ) {
    binding.aboutButton.setOnClickListener {
      uiState.newScreenPress()
    }
  }

  private fun listenOnStateChange(
    binding: TextlistBinding,
    uiState: TextListUIState,
  ) = lifecycleScope.launch {
    uiState.valuesFlow.collect {
      setupList(binding.recyclerView, it.items)
    }
  }

  private fun setupList(
    recyclerView: RecyclerView,
    items: List<String>
  ) {
    recyclerView.apply {
      layoutManager = LinearLayoutManager(this@TextListFragment.context)
      adapter = TextListRecyclerView(items, {})
    }
  }

  private fun gotoAboutFragment() {
    findNavController().navigate(
      R.id.action_textInputFragment_to_aboutFragment,
      Bundle().apply { putString("name", "GOERGE?") }
    )
  }

}

