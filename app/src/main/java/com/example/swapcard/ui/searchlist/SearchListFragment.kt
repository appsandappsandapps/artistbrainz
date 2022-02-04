package com.example.swapcard.ui.searchlist

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.swapcard.R
import com.example.swapcard.databinding.SearchlistBinding
import com.example.swapcard.viewModelWithSavedState
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class SearchListFragment : Fragment(R.layout.searchlist) {

  lateinit var viewModel: SearchListViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    val binding = SearchlistBinding.bind(view)

    viewModel = viewModelWithSavedState {
      app, savedState -> SearchListViewModel(
        app,
        savedState,
        {
          val args = Bundle().apply { this.putInt("artistId", it) }
          findNavController().navigate(R.id.go_to_artist_detail, args)
        }
      )
    }

    listenOnViews(binding, viewModel.uiState)
    listenOnStateChange(binding, viewModel.uiState)
  }

  private fun listenOnViews(
    binding: SearchlistBinding,
    uiState: SearchListUIState,
  ) {
  }

  private fun listenOnStateChange(
    binding: SearchlistBinding,
    uiState: SearchListUIState,
  ) = lifecycleScope.launch {
    uiState.valuesFlow.collect {
      setupList(binding.recyclerView, it.artists)
    }
  }

  private fun setupList(
    recyclerView: RecyclerView,
    artists: List<SearchListUIState.ArtistUI>,
  ) {
    recyclerView.apply {
      if(adapter != null) {
        val _adapter:SearchListRecyclerView = adapter as SearchListRecyclerView
        _adapter.items = artists
      } else {
        layoutManager = LinearLayoutManager(this@SearchListFragment.context)
        adapter = SearchListRecyclerView(
          artists,
          { viewModel.gotoArtistDetail(it) },
          { viewModel.bookmark(it) },
          { viewModel.debookmark(it) },
          { viewModel.paginateSearch() }
        )
      }
      recyclerView.post {
        recyclerView.adapter?.notifyDataSetChanged()
      }
    }
  }

}

