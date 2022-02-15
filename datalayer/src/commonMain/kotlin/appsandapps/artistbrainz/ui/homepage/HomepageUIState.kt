package appsandapps.artistbrainz.ui.homepage

import kotlinx.coroutines.flow.MutableStateFlow
import appsandapps.artistbrainz.utils.Parcelable
import kotlinx.parcelize.Parcelize

class HomepageUIState(
  private var existing: UIValues = UIValues(),
  private val saveToParcel: (UIValues) -> Unit = {},
) {

  @Parcelize data class UIValues(
    val bookmarked: Int = 0,
  ): Parcelable

  val valuesFlow = MutableStateFlow(existing)

  private var values
    get() = valuesFlow.value
    set(value) {
      valuesFlow.value = value
      saveToParcel(values)
    }

  // Called via the viewmodel

  fun setBookmarked(num: Int) {
    values = UIValues(num)
  }

}
