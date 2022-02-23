package appsandapps.artistbrainz.ui.homepage

import appsandapps.artistbrainz.ui.homepage.HomepageUIState.Action.*
import appsandapps.artistbrainz.utils.Parcelable
import appsandapps.artistbrainz.utils.Parcelize
import appsandapps.artistbrainz.utils.UIState
import appsandapps.artistbrainz.ui.homepage.HomepageUIState.UIValues

class HomepageUIState(
  private val viewModel: HomepageViewModel,
  private val existing: UIValues = UIValues(),
  private val saveToParcel: (UIValues) -> Unit = {},
) : UIState<UIValues>(existing, saveToParcel) {

  @Parcelize data class UIValues(
    val bookmarked: Int = 0,
  ): Parcelable

  sealed class Action {
    class Bookmarks(val num: Int): Action()
  }

  fun update(action: Action): Any = when(action) {
    is Bookmarks -> {
      stateData = stateData.copy(bookmarked = action.num)
    }
  }

}
