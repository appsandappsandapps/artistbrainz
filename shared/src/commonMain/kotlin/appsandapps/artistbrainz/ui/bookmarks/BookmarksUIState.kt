package appsandapps.artistbrainz.ui.bookmarks

import appsandapps.artistbrainz.data.Bookmark
import appsandapps.artistbrainz.ui.bookmarks.BookmarksUIState.Action.*
import appsandapps.artistbrainz.ui.bookmarks.BookmarksUIState.UIValues
import appsandapps.artistbrainz.utils.Parcelable
import appsandapps.artistbrainz.utils.Parcelize
import appsandapps.artistbrainz.utils.UIState

class BookmarksUIState(
  private val viewModel: BookmarksViewModel,
  private var existing: UIValues = UIValues(),
  private val saveToParcel: (UIValues) -> Unit = {},
) : UIState<UIValues>(existing, saveToParcel) {

  @Parcelize data class UIValues(
    val bookmarks: List<Bookmark> = listOf(),
  ): Parcelable

  sealed class Action {
    class GotoDetail(val id: String): Action()
    class Debookmark(val id: String): Action()
    class SetBookmarks(val bookmarks: List<Bookmark>): Action()
  }

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
