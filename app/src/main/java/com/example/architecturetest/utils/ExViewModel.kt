package com.example.architecturetest

import android.os.Parcelable
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSavedStateRegistryOwner
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.viewModels
import androidx.lifecycle.*
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.savedstate.SavedStateRegistryOwner
import com.example.architecturetest.ui.textinput.TextInputUIState
import com.example.architecturetest.ui.textinput.TextInputViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlin.reflect.KClass
import kotlin.reflect.KProperty

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
