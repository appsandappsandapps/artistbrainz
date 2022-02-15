package appsandapps.artistbrainz.ui.bookmarks

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import appsandapps.artistbrainz.R
import appsandapps.artistbrainz.collectStateFlow
import appsandapps.artistbrainz.databinding.BookmarksBinding
import appsandapps.artistbrainz.ui.artistdetail.ArtistDetailFragment
import appsandapps.artistbrainz.viewModelWithSavedState
import appsandapps.artistbrainz.ui.bookmarks.BookmarksUIState.UIValues
import appsandapps.artistbrainz.ui.homepage.HomepageUIState
import appsandapps.artistbrainz.utils.StateSaver
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * Allows you to see your bookmarks
 * You can alos unbookmark items
 */
class BookmarksFragment : Fragment(R.layout.bookmarks) {

  // Views from layout
  lateinit var binding: BookmarksBinding
  val recycler get() = binding.bookmarksRecyclerView
  // Utils for UIState object
  lateinit var uiState: BookmarksUIState
  fun collectUiState(f: (UIValues) -> Unit) = collectStateFlow(uiState.valuesFlow, f)

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    binding = BookmarksBinding.bind(view)

    uiState = viewModelWithSavedState { app, savedState ->
      BookmarksViewModel(
        StateSaver(savedState),
        repository = app.artistsRepository,
        dispatcher = Dispatchers.IO,
      )
    }.apply {
      // so it's added on fragment readd
      gotoDetail = { id->
        findNavController().navigate(
          R.id.go_to_artist_detail,
          Bundle().apply { putString(ArtistDetailFragment.ARG_ARTIST_ID, id) }
        )
      }
    }.uiState
    observeBookmarks()
  }

  private fun observeBookmarks() = collectUiState {
    setupList(it.bookmarks)
    recycler.post {
      recycler.adapter?.notifyDataSetChanged()
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
          { uiState.onGotoDetailScreen(it) },
          { uiState.onDebookmark(it) },
        )
      }
    }
  }

}

