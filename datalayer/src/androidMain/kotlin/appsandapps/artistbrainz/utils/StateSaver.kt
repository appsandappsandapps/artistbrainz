package appsandapps.artistbrainz.utils

import android.os.Parcelable
import androidx.lifecycle.SavedStateHandle

actual class StateSaver(
  val handle: SavedStateHandle
) {

  actual fun save(obj: Any) {
    val parcel = obj as Parcelable
    handle.setByHashCode(parcel)
  }

  actual fun <T> get(default: T): T {
    val parcel = default as Parcelable
    val ret = handle.getByHashCode(parcel)
    return ret as T
  }

}

/**
 * Gets the state by the unique hashcode for the class.
 * This means only one of this type of object can be saved,
 *   since all will have the same tag.
 *
 * USED: to get the values data class of UiState, which will
 *   have only one instance, in the ViewModel
 */
fun <T : Parcelable> SavedStateHandle.getByHashCode(initial:T) : T {
  val tag = "${initial::class.hashCode()}"
  val state:T? = this.get(tag)
  if(state == null) {
    this.set(tag, initial)
    return initial
  } else
    return state
}

/**
 * Sets the state by the unique hashcode for the class.
 * This means only one of this type of object can be saved,
 *   since all will have the same tag.
 *
 * USED: to store the values data class of UiState, which will
 *   have only one instance, in the ViewModel
 */
fun <T : Parcelable> SavedStateHandle.setByHashCode(obj:T) {
  val tag = "${obj::class.hashCode()}"
  this.set(tag, obj)
}