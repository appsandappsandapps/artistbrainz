package appsandapps.artistbrainz.ui.homepage

import appsandapps.artistbrainz.ui.homepage.Action.*
import appsandapps.artistbrainz.utils.UIModel
import appsandapps.artistbrainz.utils.Parcelable
import appsandapps.artistbrainz.utils.Parcelize

@Parcelize
data class UIValues(
  val bookmarked: Int = 0,
): Parcelable

sealed class Action {
  class Bookmarks(val num: Int): Action()
}

class HomepageUIModel(
  private val viewModel: HomepageViewModel,
  private val existing: UIValues = UIValues(),
  private val saveToParcel: (UIValues) -> Unit = {},
) : UIModel<UIValues>(existing, saveToParcel) {

  fun update(action: Action): Any = when(action) {
    is Bookmarks -> {
      stateData = stateData.copy(bookmarked = action.num)
    }
  }

}
