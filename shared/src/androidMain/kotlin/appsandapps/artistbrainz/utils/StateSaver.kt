package appsandapps.artistbrainz.utils

import androidx.lifecycle.SavedStateHandle

/**
 * Abstracts over saving UI state to SavedStateHandle
 *
 * USED: in the ViewModel, which uses this to restore
 * and save ui state.
 *
 * This is changed in KMM
 */
actual class StateSaver(
  val handle: SavedStateHandle,
) : StateSaveable {

  private fun tag(obj: Any) = "${obj::class.hashCode()}"

  override fun save(obj: Parcelable): Unit = handle.set(tag(obj), obj)

  override fun <T : Parcelable> get(default: T): T {
    val tag = tag(default)
    return handle.get<T>(tag)?.let {
      it
    } ?: run {
      handle.set(tag, default)
      default
    }
  }

}