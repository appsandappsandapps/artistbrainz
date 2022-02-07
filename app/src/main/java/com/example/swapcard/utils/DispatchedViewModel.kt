package com.example.swapcard.utils

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.*

/**
 * A ViewModel with a customer coroutine dispatcher.
 *
 * It's Dispatchers.IO if not specified
 *
 * dipatchedLaunch() uses the custom dispatcher
 */
open public class DispatchedViewModel(
  private val customDispatcher: CoroutineDispatcher = Dispatchers.IO
) : ViewModel() {

  /**
   * runs the viewmodelscrope launcher with a custom dispatcher
   */
  fun dispatchedLaunch(f: suspend CoroutineScope.() -> Unit) =
    viewModelScope.launch(customDispatcher, CoroutineStart.DEFAULT, f)

}

