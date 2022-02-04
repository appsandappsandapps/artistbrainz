package com.example.swapcard

import android.app.Activity
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.lifecycle.*

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
  val app = requireActivity().application as Application
  val fact  = object : AbstractSavedStateViewModelFactory(this, null) {
    override fun <VM : ViewModel?> create(
      key: String,
      modelClass: Class<VM>,
      handle: SavedStateHandle
    ): VM {
      return f(app, handle) as VM
    }
  }
  return ViewModelProvider(this, fact).get()
}
