package appsandapps.artistbrainz.utils

import kotlinx.coroutines.*

expect open public class DispatchedViewModel {

  /**
   * runs the viewmodelscrope launcher with a custom dispatcher
   */
  fun dispatchedLaunch(f: suspend CoroutineScope.() -> Unit): Job

}