package appsandapps.artistbrainz.utils

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.*

/**
 * A ViewModel with a customer coroutine dispatcher.
 *
 * dipatchedLaunch() uses the custom dispatcher
 */
actual open public class DispatchedViewModel constructor(
  private val customDispatcher: CoroutineDispatcher
) : ViewModel() {

  /**
   * runs the viewmodelscrope launcher with a custom dispatcher
   */
  actual fun dispatchedLaunch(f: suspend CoroutineScope.() -> Unit) {
    viewModelScope.launch(customDispatcher, CoroutineStart.DEFAULT, f)
  }

}

