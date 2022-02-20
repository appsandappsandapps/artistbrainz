package appsandapps.artistbrainz.ui.homepage

import android.os.Bundle
import android.view.View
import androidx.datastore.preferences.core.edit
import androidx.fragment.app.Fragment
import androidx.lifecycle.coroutineScope
import appsandapps.artistbrainz.*
import appsandapps.artistbrainz.databinding.AboutFragmentBinding
import appsandapps.artistbrainz.utils.PREF_KEY_COMPOSE_TOGGLE
import appsandapps.artistbrainz.utils.composeToggleSetting
import appsandapps.artistbrainz.utils.prefsDataStore
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

/**
 * Shows some info
 *
 * Allows you to switch between the view sytsem and Compose
 * versions of the app
 */
class AboutFragment : Fragment(R.layout.about_fragment) {

  lateinit var bindings: AboutFragmentBinding
  val composeSwitch get() = bindings.composeSwitch

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    bindings = AboutFragmentBinding.bind(view)

    lifecycle.coroutineScope.launch {
      requireContext().composeToggleSetting(this)
        .collect { shouldUseCompose ->
          composeSwitch.isChecked = shouldUseCompose
        }
    }

    composeSwitch.setOnCheckedChangeListener { _, checked ->
      lifecycle.coroutineScope.launch {
        delay(400) // So you see the switch switch before we switch
        requireContext().prefsDataStore.edit { settings ->
          settings[PREF_KEY_COMPOSE_TOGGLE] = checked
        }
      }
    }

  }


}