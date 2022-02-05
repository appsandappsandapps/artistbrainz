package com.example.swapcard.ui.bookmarks

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.swapcard.R
import com.example.swapcard.databinding.BookmarksBinding
import com.example.swapcard.viewModelWithSavedState
import com.example.swapcard.ui.bookmarks.BookmarksUIState.UIValues
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class BookmarksFragment : Fragment(R.layout.bookmarks) {

  // Views from layout
  lateinit var binding: BookmarksBinding
  val recycler get() = binding.recyclerView
  // Utils for UIState object
  lateinit var viewModel: BookmarksViewModel
  val uiState get() = viewModel.uiState
  fun collectUiState(f: (UIValues) -> Unit) = lifecycleScope.launch {
    viewModel.uiState.valuesFlow.collect { f(it) }
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    val binding = BookmarksBinding.bind(view)

    val gotoArtist: (String) -> Unit = { artistId ->
      findNavController().navigate(
        R.id.go_to_artist_detail,
        Bundle().apply { putString("artistId", artistId) }
      )
    }
    viewModel = viewModelWithSavedState {
      app, savedState -> BookmarksViewModel(
        app,
        savedState,
        gotoArtist,
      )
    }
    observeBookmarks()
  }

  private fun observeBookmarks() = collectUiState {
    setupList(it.bookmarks)
    recycler.post {
      recyclerView.adapter?.notifyDataSetChanged()
    }
  }

  private fun setupList(artists: List<BookmarksUIState.BookmarkUI>) {
    recycler.apply {
      if(adapter != null) {
        val _adapter: BookmarksRecyclerView = adapter as BookmarksRecyclerView
        _adapter.items = artists
      } else {
        layoutManager = LinearLayoutManager(this@BookmarksFragment.context)
        adapter = BookmarksRecyclerView(
          artists,
          { viewModel.gotoDetailScreen(it) },
          { viewModel.debookmark(it) },
        )
      }
    }
  }

}

