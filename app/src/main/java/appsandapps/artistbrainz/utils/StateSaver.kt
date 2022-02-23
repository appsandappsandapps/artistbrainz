package appsandapps.artistbrainz.utils

import androidx.lifecycle.SavedStateHandle

interface StateSavable {
  fun save(obj: Parcelable): Unit
  fun <T : Parcelable> get(defaultObj: T): T

}

class StateSaverEmpty : StateSavable {
  override fun save(obj: Parcelable) { }
  override fun <T : Parcelable> get(defaultObj: T): T {
    return defaultObj
  }
}

/**
 * Abstracts over saving UI state to SavedStateHandle
 *
 * USED: in the ViewModel, which uses this to restore
 * and save ui state.
 *
 * This is changed in KMM
 */
class StateSaver(
  val handle: SavedStateHandle
) : StateSavable {

  private fun tag(obj: Any) = "${obj::class.hashCode()}"

  override fun save(obj: Parcelable) = handle.set(tag(obj), obj)

  override fun <T : Parcelable> get(defaultObj: T): T {
    val tag = tag(defaultObj)
    return handle.get<T>(tag)?.let {
      it
    } ?: run {
      handle.set(tag, defaultObj)
      defaultObj
    }
  }

}