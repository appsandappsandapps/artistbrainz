package appsandapps.artistbrainz.ui.homepage

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.adapter.FragmentStateAdapter
import appsandapps.artistbrainz.R
import appsandapps.artistbrainz.databinding.AboutFragmentBinding
import appsandapps.artistbrainz.databinding.HomeFragmentBinding
import appsandapps.artistbrainz.ui.bookmarks.BookmarksFragment
import appsandapps.artistbrainz.ui.search.SearchListFragment
import appsandapps.artistbrainz.ui.homepage.HomepageUIState.UIValues
import appsandapps.artistbrainz.viewModelWithSavedState
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

/**
 * Reacts on new bookmarks to update the tab icon
 * Fills the recyclerview with bookmarks
 */
class AboutFragment : Fragment(R.layout.about_fragment) {

  lateinit var bindings: AboutFragmentBinding

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    bindings = AboutFragmentBinding.bind(view)
  }

}