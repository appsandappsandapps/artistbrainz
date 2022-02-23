package appsandapps.artistbrainz.utils

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.*

/**
 * A ViewModel with a customer coroutine dispatcher.
 *
 * It's Dispatchers.IO if not specified.
 * dipatchedLaunch() uses the custom dispatcher.
 *
 * USED: in the ViewModels.
 *
 * This is changed in KMM.
 */
actual open public class DispatchedViewModel(
  private val customDispatcher: CoroutineDispatcher = IODispatcher
) : ViewModel() {

  /**
   * runs the viewmodelscrope launcher with a custom dispatcher
   */
  actual fun dispatchedLaunch(f: suspend CoroutineScope.() -> Unit): Job =
    viewModelScope.launch(customDispatcher, CoroutineStart.DEFAULT, f)

}

