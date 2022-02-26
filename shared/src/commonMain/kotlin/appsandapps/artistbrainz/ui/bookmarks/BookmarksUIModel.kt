package appsandapps.artistbrainz.ui.bookmarks

import appsandapps.artistbrainz.ui.bookmarks.Action.*
import appsandapps.artistbrainz.utils.UIModel
import appsandapps.artistbrainz.data.Bookmark
import appsandapps.artistbrainz.utils.Parcelable
import appsandapps.artistbrainz.utils.Parcelize

@Parcelize
data class UIValues(
  val bookmarks: List<Bookmark> = listOf(),
): Parcelable

sealed class Action {
  class GotoDetail(val id: String): Action()
  class Debookmark(val id: String): Action()
  class SetBookmarks(val bookmarks: List<Bookmark>): Action()
}

class BookmarksUIModel(
  private val viewModel: BookmarksViewModel,
  private var existing: UIValues = UIValues(),
  private val saveToParcel: (UIValues) -> Unit = {},
) : UIModel<UIValues>(existing, saveToParcel) {

  fun update(action: Action): Any = when(action) {
    is GotoDetail -> {
      viewModel.gotoDetailScreen(action.id)
    }
    is Debookmark -> {
      viewModel.debookmark(action.id)
    }
    is SetBookmarks -> {
      stateData = stateData.copy(bookmarks = action.bookmarks)
    }
  }

}
