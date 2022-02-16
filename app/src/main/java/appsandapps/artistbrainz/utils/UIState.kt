package appsandapps.artistbrainz.utils

import kotlinx.coroutines.flow.MutableStateFlow

/**
 * Utilty class that eases working with MutableStateFlow
 *
 * Once MutableStateFlow is updated via stateData it calls
 * the saveToParcel() callback--useful if you want to save
 * the state, to a Parcel for example.
 *
 * USED: In all the UIState classes that exist in ViewModels
 * and are interacted with by the ViewModel and the View/Composable/etc
 */
open class UIState<T>(
  private val existing: T,
  private val saveToParcel: (T) -> Unit = {},
) {

  public val stateFlow = MutableStateFlow(existing)

  var stateData
    get() = stateFlow.value
    set(value) {
      stateFlow.value = value
      saveToParcel(stateData)
    }

}