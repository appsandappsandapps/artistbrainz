package appsandapps.artistbrainz

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.coroutineScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import appsandapps.artistbrainz.ui.theme.ArtistBrainzTheme
import appsandapps.artistbrainz.utils.composeToggleSetting


/**
 * Starts with the View system version of the app
 * or the Compose version of the apps.
 *
 * Waits on the preferences StateFlow (DateStore) to react on such
 *
 * Note: Changing between the fragment views version and composes
 *   version is a bit hackey.
 *   Syncing (esp bookmark number) between the two versions isn't perfect.
 *   It's meant for demonstration, not production
 */
class MainActivity : AppCompatActivity() {

  // Used to switch between compose and the viewsystem
  var baseFragmentView: View? = null

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    reactOnComposeSystemToggle(this)
  }

  // Switch between the view system and compose
  private fun reactOnComposeSystemToggle(context: Context) = lifecycle.coroutineScope.launch {
    context.composeToggleSetting(this)
      .collect { shouldUseCompose ->
        if (shouldUseCompose) {
          removeMainView()
          setContent { ArtistBrainzTheme { AppNavigationScreen() } }
        } else {
          removeMainView()
          if(baseFragmentView == null)
            baseFragmentView = layoutInflater.inflate(R.layout.navigation, null)
          mainView.addView(baseFragmentView)
        }
      }
  }

  // Used to switch between the view system and compose
  private val mainView get() =
    window.decorView.findViewById<ViewGroup>(android.R.id.content)

  // Used to switch between the view system and compose
  private fun removeMainView() {
    if (mainView.childCount > 0) mainView.removeViewAt(0)
  }

}
