package appsandapps.artistbrainz.ui.homepage

import android.os.Parcelable
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.parcelize.Parcelize
import appsandapps.artistbrainz.ui.homepage.HomepageUIState.Action.*

class HomepageUIState(
  private val viewModel: HomepageViewModel,
  private var existing: UIValues = UIValues(),
  private val saveToParcel: (UIValues) -> Unit = {},
) {

  val stateFlow = MutableStateFlow(existing)

  private var stateData
    get() = stateFlow.value
    set(value) {
      stateFlow.value = value
      saveToParcel(stateData)
    }

  @Parcelize data class UIValues(
    val bookmarked: Int = 0,
  ): Parcelable

  sealed class Action {
    class Bookmarks(val num: Int): Action()
  }

  fun update(action: Action) = when(action) {
    is Bookmarks -> {
      stateData = stateData.copy(bookmarked = action.num)
    }
  }

}
