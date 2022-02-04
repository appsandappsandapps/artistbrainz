package com.example.swapcard.ui.dashboard

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.swapcard.R
import com.example.swapcard.databinding.HomeFragmentBinding
import com.example.swapcard.ui.artistdetail.ArtistDetailViewModel
import com.example.swapcard.ui.bookmarks.BookmarksFragment
import com.example.swapcard.viewModelWithSavedState
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class HomepageFragment : Fragment(R.layout.home_fragment) {

  lateinit var viewModel: HomepageViewModel

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    val bindings = HomeFragmentBinding.bind(view)

    viewModel = viewModelWithSavedState {
        app, savedState -> HomepageViewModel(app, savedState)
    }

    val vpAdapter = PagerAdapter(
      requireActivity(),
      listOf(SearchFragment(), BookmarksFragment()),
    )
    bindings.viewpager2.adapter = vpAdapter
    TabLayoutMediator(bindings.tabLayout, bindings.viewpager2) { tab, pos ->
      tab.text = if(pos == 0) "Search" else "Bookmarks"
    }.attach()

    lifecycleScope.launch {
      viewModel.uiState.valuesFlow.collect { state ->
        TabLayoutMediator(bindings.tabLayout, bindings.viewpager2) { tab, pos ->
          tab.text = if(pos == 0) "Search" else "Bookmarks ${state.bookmarked}"
        }.attach()
      }
    }
  }

}

private class PagerAdapter(
  fa: FragmentActivity,
  val frags: List<Fragment>,
) : FragmentStateAdapter(fa) {
  override fun getItemCount(): Int = frags.size
  override fun createFragment(position: Int): Fragment = frags[position]
}