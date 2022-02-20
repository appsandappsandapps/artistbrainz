package appsandapps.artistbrainz.utils.ext

import android.content.Intent
import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSavedStateRegistryOwner
import androidx.fragment.app.Fragment
import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import appsandapps.artistbrainz.Application

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