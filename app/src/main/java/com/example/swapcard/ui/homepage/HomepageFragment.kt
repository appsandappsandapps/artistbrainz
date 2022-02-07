package com.example.swapcard.ui.homepage

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.swapcard.R
import com.example.swapcard.databinding.HomeFragmentBinding
import com.example.swapcard.ui.bookmarks.BookmarksFragment
import com.example.swapcard.ui.search.SearchListFragment
import com.example.swapcard.ui.homepage.HomepageUIState.UIValues
import com.example.swapcard.viewModelWithSavedState
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

/**
 * Reacts on new bookmarks to update the tab icon
 * Fills the recyclerview with bookmarks
 */
class HomepageFragment : Fragment(R.layout.home_fragment) {

  // Views
  lateinit var bindings: HomeFragmentBinding
  val tabs get() = bindings.homepageTabLayout
  val viewpager get() = bindings.homepageViewpager2
  // UI State
  lateinit var uiState: HomepageUIState
  fun collectUiState(f: (UIValues) -> Unit) = lifecycleScope.launch {
    uiState.valuesFlow.collect { f(it) }
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    bindings = HomeFragmentBinding.bind(view)

    uiState = viewModelWithSavedState {
      app, savedState -> HomepageViewModel(app, savedState)
    }.uiState

    setupViewPager()
    observeBookmarks()
  }

  private fun setupViewPager() {
    val vpAdapter = PagerAdapter(
      requireActivity(),
      listOf(SearchListFragment(), BookmarksFragment()),
    )
    viewpager.adapter = vpAdapter
  }

  private fun observeBookmarks() = collectUiState { state ->
    TabLayoutMediator(tabs, viewpager) { tab, pos ->
      tab.text = if(pos == 0) "Artists" else "Bookmarks ${state.bookmarked}"
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