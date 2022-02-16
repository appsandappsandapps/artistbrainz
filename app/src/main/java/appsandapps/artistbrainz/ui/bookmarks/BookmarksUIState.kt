package appsandapps.artistbrainz.ui.bookmarks

import android.os.Parcelable
import appsandapps.artistbrainz.data.Bookmark
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.parcelize.Parcelize
import appsandapps.artistbrainz.ui.bookmarks.BookmarksUIState.Action.*

class BookmarksUIState(
  private val viewModel: BookmarksViewModel,
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
    val bookmarks: List<Bookmark> = listOf(),
  ): Parcelable

  sealed class Action {
    class GotoDetail(val id: String): Action()
    class Debookmark(val id: String): Action()
    class SetBookmarks(val bookmarks: List<Bookmark>): Action()
  }

  fun update(action: Action) = when(action) {
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
