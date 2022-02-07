package com.example.swapcard.ui.artistdetail

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.swapcard.R
import com.example.swapcard.databinding.ArtistdetailBinding
import com.example.swapcard.viewModelWithSavedState
import com.example.swapcard.ui.artistdetail.ArtistDetailUIState.UIValues
import com.example.swapcard.utils.pluralise
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

/**
 * Loads the artist details
 * You can bookmark from here too
 */
class ArtistDetailFragment : Fragment(R.layout.artistdetail) {

  companion object {
    val ARG_ARTIST_ID = "artistID"
  }

  // Views from layout
  lateinit var binding: ArtistdetailBinding
  val checkbox get() = binding.checkbox
  val artist get() = binding.artistName
  val disambiguation get() = binding.disambiguation
  val rating get() = binding.rating
  val loading get() = binding.linearprogress
  // Utils for UIState object
  lateinit var uiState: ArtistDetailUIState
  fun collectUiState(f: (UIValues) -> Unit) = lifecycleScope.launch {
    uiState.valuesFlow.collect { f(it) }
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    binding = ArtistdetailBinding.bind(view)
    var artistId = arguments?.getString(ARG_ARTIST_ID) ?: "-1"

    uiState = viewModelWithSavedState {
      app, savedState -> ArtistDetailViewModel(app, savedState, artistId)
    }.uiState

    reactOnCheckbox()
    observeArtist()
    observeLoadingState()
    observeErrorState()
  }

  private fun reactOnCheckbox() {
    checkbox.setOnClickListener {
      if(checkbox.isChecked)
        uiState.bookmark()
      else uiState.debookmark()
    }
  }

  private fun observeLoadingState() = collectUiState {
    if(it.loading) loading.show() else loading.hide()
  }

  private fun observeErrorState() = collectUiState {
    if(it.error.isNotBlank()) {
      Toast.makeText(requireContext(), it.error, Toast.LENGTH_LONG).show()
    }
  }

  private fun observeArtist() = collectUiState {
    if(!it.loading) {
      artist.text = it.name
      checkbox.isChecked = it.bookmarked
      checkbox.visibility = View.VISIBLE
      disambiguation.text = it.disambiguation
      rating.text = "Rating ${it.rating} (${"vote".pluralise(it.voteCount)})"
    }
  }

}

