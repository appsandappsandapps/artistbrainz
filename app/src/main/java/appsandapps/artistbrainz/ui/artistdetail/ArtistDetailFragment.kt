package appsandapps.artistbrainz.ui.artistdetail

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import appsandapps.artistbrainz.R
import appsandapps.artistbrainz.collectStateFlow
import appsandapps.artistbrainz.databinding.ArtistdetailBinding
import appsandapps.artistbrainz.gotoUrl
import appsandapps.artistbrainz.viewModelWithSavedState
import appsandapps.artistbrainz.ui.artistdetail.ArtistDetailUIState.UIValues
import appsandapps.artistbrainz.utils.ext.pluralise
import appsandapps.artistbrainz.ui.artistdetail.ArtistDetailUIState.Action.*
import appsandapps.artistbrainz.utils.StateSaver

/**
 * Loads the artist details
 * You can bookmark from here too
 */
class ArtistDetailFragment : Fragment(R.layout.artistdetail) {

  companion object {
    val ARG_ARTIST_ID = "artistID"
  }

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
  lateinit var uiState: appsandapps.artistbrainz.ui.artistdetail.ArtistDetailUIState
  fun collectUiState(f: (UIValues) -> Unit) = collectStateFlow(uiState.stateFlow, f)

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    binding = ArtistdetailBinding.bind(view)

    var artistId = arguments?.getString(ARG_ARTIST_ID) ?: "-1"

    uiState = viewModelWithSavedState {
      appsandapps.artistbrainz.ui.artistdetail.ArtistDetailViewModel(
        StateSaver(it),
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
        uiState.update(Bookmark())
      else uiState.update(Debookmark())
    }
  }

  private fun reactOnLastFmButton() {
    lastFmButton.setOnClickListener {
      uiState.update(ViewLastFm())
    }
  }

  private fun reactOnYoutubeButton() {
    searchOnYoutube.setOnClickListener {
      uiState.update(SearchYoutube())
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
    if(!it.loading && it.error.isBlank()) {
      mainLayout.visibility = View.VISIBLE
      artist.text = it.artist.name
      checkbox.isChecked = it.artist.bookmarked
      if(it.artist.lastFMUrl.isBlank()) {
        lastFmButton.visibility = View.GONE
      }
      summary.setText(it.artist.summary)
      disambiguation.text = it.artist.disambiguation
      rating.text = "Rating ${it.artist.rating.value} (${"vote".pluralise(it.artist.rating.voteCount)})"
    }
  }

}

