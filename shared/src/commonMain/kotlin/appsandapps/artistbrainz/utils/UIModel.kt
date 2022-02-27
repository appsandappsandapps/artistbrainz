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
open class UIModel<T : Parcelable>(
  private val existing: T,
  private val stateSaver: StateSaveable,
) {

  public val stateFlow = MutableStateFlow(existing)

  protected var stateData
    get() = stateFlow.value
    set(value) {
      stateFlow.value = value
      stateSaver.save(stateData)
    }

}