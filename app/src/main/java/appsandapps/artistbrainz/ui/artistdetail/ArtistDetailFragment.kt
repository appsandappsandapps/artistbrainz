package appsandapps.artistbrainz.ui.artistdetail

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import appsandapps.artistbrainz.R
import appsandapps.artistbrainz.collectStateFlow
import appsandapps.artistbrainz.databinding.ArtistdetailBinding
import appsandapps.artistbrainz.gotoUrl
import appsandapps.artistbrainz.viewModelWithSavedState
import appsandapps.artistbrainz.ui.artistdetail.ArtistDetailUIState.UIValues
import appsandapps.artistbrainz.utils.pluralise
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
  val searchOnYoutube get() = binding.searchOnYoutubeButton
  val artist get() = binding.artistName
  val disambiguation get() = binding.disambiguation
  val rating get() = binding.rating
  val loading get() = binding.linearprogress
  val summary get() = binding.summaryText
  val mainLayout get() = binding.artistdetailMainlayout
  val lastFmButton get() = binding.viewOnLastfmButton
  lateinit var uiState: ArtistDetailUIState
  fun collectUiState(f: (UIValues) -> Unit) = collectStateFlow(uiState.valuesFlow, f)

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    binding = ArtistdetailBinding.bind(view)

    var artistId = arguments?.getString(ARG_ARTIST_ID) ?: "-1"

    uiState = viewModelWithSavedState {
      app, savedState -> ArtistDetailViewModel(
        app,
        savedState,
        artistId,
      )
    }.apply {
      // reattach on every new fragment
      gotoUrlCallback = { gotoUrl(it) }
    }.uiState

    reactOnBookmarkCheckbox()
    reactOnYoutubeButton()
    reactOnLastFmButton()
    observeArtist()
    observeLoadingState()
    observeErrorState()
  }

  private fun reactOnBookmarkCheckbox() {
    checkbox.setOnClickListener {
      if(checkbox.isChecked)
        uiState.onBookmark()
      else uiState.onDebookmark()
    }
  }

  private fun reactOnLastFmButton() {
    lastFmButton.setOnClickListener {
      uiState.onLastFmButton()
    }
  }

  private fun reactOnYoutubeButton() {
    searchOnYoutube.setOnClickListener {
      uiState.onSearchYoutube()
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
      mainLayout.visibility = View.VISIBLE
      artist.text = it.name
      checkbox.isChecked = it.bookmarked
      if(it.lastFmUrl.isBlank()) {
        lastFmButton.visibility = View.GONE
      }
      summary.setText(it.summary)
      disambiguation.text = it.disambiguation
      rating.text = "Rating ${it.rating} (${"vote".pluralise(it.voteCount)})"
    }
  }

}

