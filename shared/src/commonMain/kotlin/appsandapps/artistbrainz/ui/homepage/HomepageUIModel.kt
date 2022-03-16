package appsandapps.artistbrainz.ui.homepage

import appsandapps.artistbrainz.ui.homepage.HomepageAction.*
import appsandapps.artistbrainz.utils.*

@Parcelize
data class HomepageUIValues(
  val bookmarked: Int = 0,
): Parcelable

sealed class HomepageAction {
  class Bookmarks(val num: Int): HomepageAction()
}

class HomepageUIModel(
  private val viewModel: HomepageViewModel,
  private val stateSaver: StateSaveable,
) : UIModel<HomepageUIValues>(stateSaver.get(HomepageUIValues()), stateSaver) {

  fun update(action: HomepageAction): Any = when(action) {
    is Bookmarks -> {
      stateData = stateData.copy(bookmarked = action.num)
    }
  }

}
