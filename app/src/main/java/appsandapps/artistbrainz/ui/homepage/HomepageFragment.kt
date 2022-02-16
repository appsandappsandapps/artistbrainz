package appsandapps.artistbrainz.ui.homepage

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import appsandapps.artistbrainz.R
import appsandapps.artistbrainz.collectStateFlow
import appsandapps.artistbrainz.databinding.HomeFragmentBinding
import appsandapps.artistbrainz.ui.bookmarks.BookmarksFragment
import appsandapps.artistbrainz.ui.homepage.HomepageUIState.UIValues
import appsandapps.artistbrainz.ui.search.SearchListFragment
import appsandapps.artistbrainz.utils.StateSaver
import appsandapps.artistbrainz.viewModelWithSavedState
import com.google.android.material.badge.BadgeDrawable
import com.google.android.material.badge.BadgeUtils
import com.google.android.material.tabs.TabLayoutMediator

/**
 * Reacts on new bookmarks to update the tab icon
 * Fills the recyclerview with bookmarks
 */
class HomepageFragment : Fragment(R.layout.home_fragment) {

  // Views
  lateinit var bindings: HomeFragmentBinding
  val tabs get() = bindings.homepageTabLayout
  val viewpager get() = bindings.homepageViewpager2
  lateinit var uiState: HomepageUIState
  fun collectUiState(f: (UIValues) -> Unit) = collectStateFlow(uiState.stateFlow, f)

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    bindings = HomeFragmentBinding.bind(view)

    uiState = viewModelWithSavedState {
      app, savedState -> HomepageViewModel(app, StateSaver(savedState))
    }.uiState

    setupViewPager()
    observeBookmarks()
  }

  private fun setupViewPager() {
    val vpAdapter = PagerAdapter(
      requireActivity(),
      listOf(SearchListFragment(), BookmarksFragment(), AboutFragment()),
    )
    viewpager.adapter = vpAdapter
  }

  private fun observeBookmarks() = collectUiState { state ->
    TabLayoutMediator(tabs, viewpager) { tab, pos ->
      tab.text = if(pos == 0) "Artists"
      else if(pos == 1) "Bookmarks"// ${state.bookmarked}"
      else "About"
      if(pos == 1) {
        tabs.post {
          BadgeDrawable.create(requireContext()).apply {
            number = state.bookmarked
            verticalOffset = tabs.height / 4
            horizontalOffset = tab.view.paddingEnd / 2
            BadgeUtils.attachBadgeDrawable(this, tab.view)
          }
        }
      }
    }.attach()
  }

}

private class PagerAdapter(
  fa: FragmentActivity,
  val frags: List<Fragment>,
) : FragmentStateAdapter(fa) {
  override fun getItemCount(): Int = frags.size
  override fun createFragment(position: Int): Fragment = frags[position]
}