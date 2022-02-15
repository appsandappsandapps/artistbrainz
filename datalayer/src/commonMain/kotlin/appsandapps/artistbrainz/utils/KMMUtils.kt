package appsandapps.artistbrainz.utils

import android.os.Parcelable
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

expect interface Parcelable

@OptIn(ExperimentalMultiplatform::class)
@OptionalExpectation
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.BINARY)
expect annotation class Parcelize

expect class StateSaver {
  fun save(obj: Any)
  fun <T> get(default: T): T
}

expect open public class DispatchedViewModel {

  /**
   * runs the viewmodelscrope launcher with a custom dispatcher
   */
  fun dispatchedLaunch(f: suspend CoroutineScope.() -> Unit)

}
