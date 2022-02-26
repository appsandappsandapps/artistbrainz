package appsandapps.artistbrainz.ui.search

import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import appsandapps.artistbrainz.*
import appsandapps.artistbrainz.data.Artist
import appsandapps.artistbrainz.databinding.SearchlistFragmentBinding
import appsandapps.artistbrainz.ui.artistdetail.ArtistDetailFragment
import appsandapps.artistbrainz.ui.search.SearchAction.*
import appsandapps.artistbrainz.ui.search.SearchUIValues
import appsandapps.artistbrainz.ui.search.SearchListUIModel
import appsandapps.artistbrainz.utils.StateSaver

/**
 * Reacts on new searches
 * Fills the recyclerview with results
 */
class SearchListFragment : Fragment(R.layout.searchlist_fragment) {

  // Views from layout
  lateinit var binding: SearchlistFragmentBinding
  val recycler get() = binding.searchlistRecyclerView
  val input get() = binding.edittext
  val clearText get() = binding.searchClearText
  val loading get() = binding.progressIndicator
  val emptyList get() = binding.emptyList
  lateinit var uiState: SearchListUIModel
  fun collectUiState(f: (SearchUIValues) -> Unit) = collectStateFlow(uiState.stateFlow, f)

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    binding = SearchlistFragmentBinding.bind(view)
    uiState = viewModelWithSavedState {
      appsandapps.artistbrainz.ui.search.SearchListViewModel(
        StateSaver(it),
      )
    }.apply {
      // reattach on every new fragment
      gotoDetail = { id:String ->
        findNavController().navigate(
          R.id.go_to_artist_detail,
          Bundle().apply { putString(ArtistDetailFragment.ARG_ARTIST_ID, id) }
        )
      }
    }.uiState

    reactOnClearButton()
    reactOnSearchField()
    observerLoadingState()
    observeErrorState()
    observeClearTextIcon()
    observeArtistResults()
    observeEmptyList()
  }

  private fun reactOnClearButton() {
    clearText.setOnClickListener {
      uiState.update(ClearSearch())
      input.setText("")
    }
  }

  private fun reactOnSearchField() {
    input.doAfterTextChanged { uiState.update(TypedSearch("$it")) }
    input.setOnEditorActionListener { _, id, _ ->
      if(id == EditorInfo.IME_ACTION_SEARCH) {
        uiState.update(PressSearch())
        hideKeyboard()
        true
      } else {
        false
      }
    }
  }

  private fun observeErrorState() = collectUiState {
    if(it.error.isNotBlank()) {
      Toast.makeText(requireContext(), it.error, Toast.LENGTH_LONG).show()
      view?.postDelayed({
        uiState.update(ServerError(""))
      }, 1000)
    }
  }

  private fun observeClearTextIcon() = collectUiState {
    clearText.visibility =
      if (it.showClearText()) View.VISIBLE else View.GONE
  }

  private fun observeEmptyList() = collectUiState {
    emptyList.visibility = if(it.hasNoResults && !it.isBeforeFirstSearch)
      View.VISIBLE
    else View.INVISIBLE
  }

  private fun observerLoadingState() = collectUiState {
    if(it.loading) loading.show() else loading.hide()
  }

  private fun observeArtistResults() = collectUiState{
    setupSearchListAdapter(it.artists)
    recycler.post {
      recycler.adapter?.notifyDataSetChanged()
    }
  }

  private fun setupSearchListAdapter(
    artists: List<Artist>,
  ) {
    recycler.apply {
      if(adapter != null && artists.size > 0) {
        val _adapter = adapter as SearchListRecyclerView
        _adapter.items = artists
      } else {
        layoutManager = LinearLayoutManager(this.context)
        adapter = SearchListRecyclerView(
          artists,
          { uiState.update(GotoArtistDetail(it)) },
          { id, name -> uiState.update(Bookmark(id, name)) },
          { uiState.update(Debookmark(it)) },
          { uiState.update(PaginateSearch()) }
        )
      }
    }
  }

}

