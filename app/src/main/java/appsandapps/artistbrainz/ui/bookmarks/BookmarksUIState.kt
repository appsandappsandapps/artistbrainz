package appsandapps.artistbrainz.ui.bookmarks

import android.os.Parcelable
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.parcelize.Parcelize

class BookmarksUIState(
  private val viewModel: BookmarksViewModel,
  private var existing: UIValues = UIValues(),
  private val saveToParcel: (UIValues) -> Unit = {},
) {

  @Parcelize
  data class BookmarkUI (
    val id: String = "",
    val name: String = "",
  ): Parcelable

  @Parcelize data class UIValues(
    val bookmarks: List<BookmarkUI> = listOf(),
  ): Parcelable

  val valuesFlow = MutableStateFlow(existing)

  private var values
    get() = valuesFlow.value
    set(value) {
      valuesFlow.value = value
      saveToParcel(values)
    }

  // Called via the view/composable

  fun onDebookmark(id: String) {
    viewModel.debookmark(id)
  }

  fun onGotoDetailScreen(id: String) {
    viewModel.gotoDetailScreen(id)
  }

  // Called via the view model

  fun setBookmarks(bookmarks: UIValues) {
    values = bookmarks
  }

}
