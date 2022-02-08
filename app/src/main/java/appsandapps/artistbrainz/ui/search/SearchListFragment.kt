package appsandapps.artistbrainz.ui.search

import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import appsandapps.artistbrainz.R
import appsandapps.artistbrainz.databinding.SearchlistFragmentBinding
import appsandapps.artistbrainz.hideKeyboard
import appsandapps.artistbrainz.ui.artistdetail.ArtistDetailFragment
import appsandapps.artistbrainz.viewModelWithSavedState
import appsandapps.artistbrainz.ui.search.SearchListUIState.UIValues
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

/**
 * Reacts on new searches
 * Fills the recyclerview with results
 */
class SearchListFragment : Fragment(R.layout.searchlist_fragment) {

  var beforeFirstSearch = true
  // Views from layout
  lateinit var binding: SearchlistFragmentBinding
  val recycler get() = binding.searchlistRecyclerView
  val input get() = binding.edittext
  val clearText get() = binding.searchClearText
  val loading get() = binding.progressIndicator
  val emptyList get() = binding.emptyList
  // Utils for UIState object
  lateinit var uiState: SearchListUIState
  fun collectUiState(f: (UIValues) -> Unit) = lifecycleScope.launch {
    uiState.valuesFlow.collect { f(it) }
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    binding = SearchlistFragmentBinding.bind(view)
    uiState = viewModelWithSavedState {
      app, savedState -> SearchListViewModel(
        app,
        savedState,
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
      uiState.onClearTextPressed()
      input.setText("")
    }
  }

  private fun reactOnSearchField() {
    input.doAfterTextChanged { uiState.onTypedSearchQuery("$it") }
    input.setOnEditorActionListener { _, id, _ ->
      if(id == EditorInfo.IME_ACTION_SEARCH) {
        uiState.onPressEnter()
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
    }
  }

  private fun observeClearTextIcon() = collectUiState {
    clearText.visibility =
      if (it.showClearText()) View.VISIBLE else View.GONE
  }

  private fun observeEmptyList() = collectUiState {
    emptyList.visibility = if(it.emptyList && !beforeFirstSearch)
      View.VISIBLE
    else View.INVISIBLE
  }

  private fun observerLoadingState() = collectUiState {
    if(it.loading) loading.show() else loading.hide()
  }

  private fun observeArtistResults() = collectUiState{
    if(it.artists.size > 0) beforeFirstSearch = false
    setupSearchListAdapter(it.artists)
    recycler.post {
      recycler.adapter?.notifyDataSetChanged()
    }
  }

  private fun setupSearchListAdapter(
    artists: List<SearchListUIState.ArtistUI>,
  ) {
    recycler.apply {
      if(adapter != null && artists.size > 0) {
        val _adapter = adapter as SearchListRecyclerView
        _adapter.items = artists
      } else {
        layoutManager = LinearLayoutManager(this.context)
        adapter = SearchListRecyclerView(
          artists,
          { uiState.onGotoArtistDetail(it) },
          { id, name -> uiState.onBookmark(id, name) },
          { uiState.onDebookmark(it) },
          { uiState.onPaginateSearch() }
        )
      }
    }
  }

}

