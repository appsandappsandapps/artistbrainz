package com.example.swapcard.ui.search

import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.swapcard.R
import com.example.swapcard.databinding.SearchlistBinding
import com.example.swapcard.hideKeyboard
import com.example.swapcard.viewModelWithSavedState
import com.example.swapcard.ui.search.SearchListUIState.UIValues
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class SearchListFragment : Fragment(R.layout.searchlist) {

  var beforeFirstSearch = true
  // Views from layout
  lateinit var binding: SearchlistBinding
  val recycler get() = binding.recyclerView
  val input get() = binding.edittext
  val loading get() = binding.progressIndicator
  val emptyList get() = binding.emptyList
  // Utils for UIState object
  lateinit var viewModel: SearchListViewModel
  val uiState get() = viewModel.uiState
  fun collectUiState(f: (UIValues) -> Unit) = lifecycleScope.launch {
    viewModel.uiState.valuesFlow.collect { f(it) }
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    binding = SearchlistBinding.bind(view)
    val gotoDetail = { id:String ->
      findNavController().navigate(
        R.id.go_to_artist_detail,
        Bundle().apply { putString("artistId", id) }
      )
    }
    viewModel = viewModelWithSavedState {
      app, savedState -> SearchListViewModel(
        app,
        savedState,
        gotoDetail,
      )
    }

    reactOnSearchField()
    observerLoadingState()
    observeErrorState()
    observeArtistResults()
    observeEmptyList()
  }

  private fun reactOnSearchField() {
    input.doAfterTextChanged { uiState.setInputText("$it") }
    input.setOnEditorActionListener { _, id, _ ->
      if(id == EditorInfo.IME_ACTION_DONE) {
        uiState.pressEnter()
        hideKeyboard()
        true
      }
      false
    }
  }

  private fun observeErrorState() = collectUiState {
    if(it.error.isNotBlank()) {
      Toast.makeText(requireContext(), it.error, Toast.LENGTH_LONG).show()
    }
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
          { viewModel.gotoArtistDetail(it) },
          { id, name -> viewModel.bookmark(id, name) },
          { viewModel.debookmark(it) },
          { viewModel.paginateSearch() }
        )
      }
    }
  }

}

