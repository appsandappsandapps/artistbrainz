package com.example.swapcard

import android.os.Parcelable
import androidx.lifecycle.*

/**
 * Gets the state by the unique hashcode for the class.
 * This means only one of this type of object can be saved,
 *   since all will have the same tag.
 *
 * USED: to get the values data class of UiState, which will
 *   have only one instance, in the ViewModel
 */
fun <T : Parcelable> SavedStateHandle.getByHashCode(initial:T) : T {
  val tag = "${initial::class.java.hashCode()}"
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
  val tag = "${obj::class.java.hashCode()}"
  this.set(tag, obj)
}

/**
 * Eases making ViewModels with
 * - Application Context
 * - SavedStateHandle
 * - default parameters.
 * in Composable
 *
 * USED: in the *Screen composibles to start the view models
 * for the component.
 */
/*
@Composable
inline fun <reified VM: ViewModel> viewModelWithSavedState(
  crossinline f: (Application, SavedStateHandle) -> VM
) : VM {
  val savedStateOwner = LocalSavedStateRegistryOwner.current
  val app = LocalContext.current.applicationContext as Application
  val fact  = object : AbstractSavedStateViewModelFactory(savedStateOwner, null) {
    override fun <VM : ViewModel?> create(
      key: String,
      modelClass: Class<VM>,
      handle: SavedStateHandle
    ): VM = f(app, handle) as VM
  }
  return viewModel<VM>(factory = fact)
}
*/