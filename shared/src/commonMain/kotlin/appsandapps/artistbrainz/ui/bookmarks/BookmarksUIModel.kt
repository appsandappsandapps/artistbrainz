package appsandapps.artistbrainz.ui.bookmarks

import appsandapps.artistbrainz.ui.bookmarks.BookmarksAction.*
import appsandapps.artistbrainz.data.Bookmark
import appsandapps.artistbrainz.utils.*

@Parcelize
data class BookmarksUIValues(
  val bookmarks: List<Bookmark> = listOf(),
): Parcelable

sealed class BookmarksAction {
  class GotoDetail(val id: String): BookmarksAction()
  class Debookmark(val id: String): BookmarksAction()
  class SetBookmarks(val bookmarks: List<Bookmark>): BookmarksAction()
}

class BookmarksUIModel(
  private val viewModel: BookmarksViewModel,
  private val stateSaver: StateSaveable,
) : UIModel<BookmarksUIValues>(stateSaver.get(BookmarksUIValues()), stateSaver) {

  fun update(action: BookmarksAction): Any = when(action) {
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
