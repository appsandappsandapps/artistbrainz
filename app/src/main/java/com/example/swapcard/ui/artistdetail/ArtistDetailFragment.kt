package com.example.swapcard.ui.artistdetail

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.swapcard.R
import com.example.swapcard.databinding.ArtistdetailBinding
import com.example.swapcard.viewModelWithSavedState
import com.example.swapcard.ui.search.ArtistDetailUIState.UIValues
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class ArtistDetailFragment : Fragment(R.layout.artistdetail) {

  // Views from layout
  lateinit var binding: ArtistDetailBinding
  val checkbox get() binding.checkbox
  val artist get() binding.aristName
  val disambiguation get() binding.disambiguation
  val rating get() binding.rating
  val voteCount get() binding.voteCount
  // Utils for UIState object
  lateinit var viewModel: ArtistDetailViewModel
  val uiState get() = viewModel.uiState
  fun collectUiState(f: (UIValues) -> Unit) = lifecycleScope.launch {
    viewModel.uiState.valuesFlow.collect { f(it) }
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    binding = ArtistdetailBinding.bind(view)
    var artistId = arguments?.getString("artistId") ?: "-1"

    viewModel = viewModelWithSavedState {
      app, savedState -> ArtistDetailViewModel(app, savedState, artistId)
    }

    reactOnCheckbox()
    observeArtist()
  }

  private fun reactOnCheckbox() {
    checkbox.setOnClickListener {
      if(checkbox.isChecked)
        viewModel.bookmark(binding.artistName.text.toString())
      else viewModel.debookmark()
    }
  }

  private fun observeArtist() = collectUiState {
    artist.text = it.name
    checkbox.isChecked = it.bookmarked
    disambiguation.text = it.disambiguation
    rating.text = it.rating.toString()
    voteCount.text = it.voteCount.toString()
  }

}

