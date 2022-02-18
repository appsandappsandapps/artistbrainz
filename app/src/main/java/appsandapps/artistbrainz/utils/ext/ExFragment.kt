package appsandapps.artistbrainz

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.lifecycle.*
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

/**
 * Hides the soft input keyboard from within a fragment.
 *
 * USED: in a fragment to hide the keyboard
 */
fun Fragment.hideKeyboard() {
  val imm: InputMethodManager =
    context?.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
  imm.hideSoftInputFromWindow(view?.windowToken, 0)
}

/**
 * Starts collecting a stateflow in the fragment's lifecycle scope
 * and uses `repeatOnLifecycle` to make sure we don't collect
 * when the fragment has disappeared
 *
 * Used: All the fragments to collect the UI state
 */
fun <T> Fragment.collectStateFlow(
  stateFlow: StateFlow<T>,
  collector: (T) -> Unit
) {
  lifecycleScope.launch {
    lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
      stateFlow.collect { collector(it) }
    }
  }
}

/**
 * Eases making ViewModels with
 * - Application Context
 * - SavedStateHandle
 * - default parameters
 * in a Frgament
 *
 * USED: in a fragment to initialise a ViewModel
 */
inline fun <reified VM: ViewModel> Fragment.viewModelWithSavedState(
  crossinline f: (Application, SavedStateHandle) -> VM
): VM {
  val fact  = object : AbstractSavedStateViewModelFactory(this, null) {
    override fun <VM1: ViewModel> create(
      key: String,
      modelClass: Class<VM1>,
      handle: SavedStateHandle
    ): VM1 = f(
      requireActivity().application as Application,
      handle
    ) as VM1
  }
  return ViewModelProvider(this, fact).get()
}

fun Fragment.gotoUrl(url: String) {
  startActivity(Intent(Intent.ACTION_VIEW).apply {
    setData(
      Uri.parse(url)
    )
  })
}
