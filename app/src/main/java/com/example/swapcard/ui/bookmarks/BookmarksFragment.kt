package com.example.swapcard.ui.bookmarks

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.swapcard.R
import com.example.swapcard.databinding.BookmarksBinding
import com.example.swapcard.viewModelWithSavedState
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class BookmarksFragment : Fragment(R.layout.bookmarks) {

  lateinit var bookmakrsViewModel: BookmarksViewModel

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    val binding = BookmarksBinding.bind(view)

    bookmakrsViewModel = viewModelWithSavedState {
      app, savedState -> BookmarksViewModel(app, savedState)
    }

    listenOnStateChange(binding, bookmakrsViewModel.uiState)
  }

  private fun listenOnStateChange(
    binding: BookmarksBinding,
    uiState: BookmarksUIState,
  ) = lifecycleScope.launch {
    uiState.valuesFlow.collect {
      setupList(binding.recyclerView, it.bookmarks)
    }
  }

  private fun setupList(
    recyclerView: RecyclerView,
    artists: List<BookmarksUIState.BookmarkUI>,
  ) {
    recyclerView.apply {
      if(adapter != null) {
        val _adapter: BookmarksRecyclerView = adapter as BookmarksRecyclerView
        _adapter.items = artists
      } else {
        layoutManager = LinearLayoutManager(this@BookmarksFragment.context)
        adapter = BookmarksRecyclerView(
          artists,
          { bookmakrsViewModel.debookmark(it) },
        )
      }
      recyclerView.post {
        recyclerView.adapter?.notifyDataSetChanged()
      }
    }
  }

}

