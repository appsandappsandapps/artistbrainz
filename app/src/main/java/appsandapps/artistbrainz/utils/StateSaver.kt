package appsandapps.artistbrainz.utils

import androidx.lifecycle.SavedStateHandle

typealias Parcelable = android.os.Parcelable
typealias Parcelize = kotlinx.android.parcel.Parcelize

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
) {

  private fun tag(obj: Any) = "${obj::class.hashCode()}"

  fun save(obj: Parcelable) = handle.set(tag(obj), obj)

  fun <T : Parcelable> get(defaultObj: T): T {
    val tag = tag(defaultObj)
    return handle.get<T>(tag)?.let {
      it
    } ?: run {
      handle.set(tag, defaultObj)
      defaultObj
    }
  }

}