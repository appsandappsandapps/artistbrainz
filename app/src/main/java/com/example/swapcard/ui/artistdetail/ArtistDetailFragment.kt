package com.example.swapcard.ui.artistdetail

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.swapcard.R
import com.example.swapcard.databinding.ArtistdetailBinding
import com.example.swapcard.viewModelWithSavedState
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class ArtistDetailFragment : Fragment(R.layout.artistdetail) {

  lateinit var viewModel: ArtistDetailViewModel

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    val binding = ArtistdetailBinding.bind(view)
    var artistId = arguments?.getInt("artistId") ?: -1

    viewModel = viewModelWithSavedState {
      app, savedState -> ArtistDetailViewModel(app, savedState, artistId)
    }

    listenOnViews(binding, viewModel.uiState)
    listenOnStateChange(binding, viewModel.uiState)
  }

  private fun listenOnViews(
    binding: ArtistdetailBinding,
    uiState: ArtistDetailUIState,
  ) {
    binding.checkbox.setOnClickListener {
      if(binding.checkbox.isChecked) viewModel.bookmark()
      else viewModel.debookmark()
    }
  }

  private fun listenOnStateChange(
    binding: ArtistdetailBinding,
    uiState: ArtistDetailUIState,
  ) = lifecycleScope.launch {
    uiState.valuesFlow.collect {
      binding.artistName.text = it.name
      binding.checkbox.isChecked = it.bookmarked
      binding.disambiguation.text = it.disambiguation
      binding.rating.text = it.rating.toString()
      binding.voteCount.text = it.voteCount.toString()
    }
  }

}

