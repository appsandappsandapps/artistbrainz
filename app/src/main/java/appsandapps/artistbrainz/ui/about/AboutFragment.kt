package appsandapps.artistbrainz.ui.homepage

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import appsandapps.artistbrainz.R
import appsandapps.artistbrainz.databinding.AboutFragmentBinding

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