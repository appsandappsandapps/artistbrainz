package appsandapps.artistbrainz.ui.artistdetail

import android.os.Parcelable
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.parcelize.Parcelize

class ArtistDetailUIState(
  private val viewModel: ArtistDetailViewModel,
  private var existing: UIValues = UIValues(),
  private val saveToParcel: (UIValues) -> Unit = {},
) {

  @Parcelize data class UIValues(
    val id: String = "",
    val name: String = "",
    val bookmarked: Boolean = false,
    val disambiguation: String = "",
    val voteCount: Int = 0,
    val rating: Double = 0.0,
    val loading: Boolean = true,
    val error: String = "",
    val lastFmUrl: String = "",
    val summary: String = "",
  ): Parcelable

  val valuesFlow = MutableStateFlow(existing)

  private var values
    get() = valuesFlow.value
    set(value) {
      valuesFlow.value = value
      saveToParcel(values)
    }

  // Called via the view/composable

  fun onBookmark() {
    viewModel.bookmark(values.id, values.name)
  }

  fun onDebookmark() {
    viewModel.debookmark()
  }

  fun onSearchYoutube() {
    viewModel.searchYoutube(values.name)
  }

  fun onLastFmButton() {
    viewModel.viewOnLastFm(values.lastFmUrl)
  }

  // Called via the viewmodel

  fun setArtist(
    id: String,
    name: String,
    bookmarked: Boolean = false,
    disambiguation: String = "",
    rating: Double = 0.0,
    voteCount: Int = 0,
    summary: String = "",
    lastFmUrl: String = "",
  ) {
    values = UIValues(
      id = id,
      name = name,
      bookmarked = bookmarked,
      disambiguation = disambiguation,
      voteCount = voteCount,
      rating = rating,
      error = "",
      loading = false,
      summary = summary,
      lastFmUrl = lastFmUrl,
    )
  }

  fun setError(e: String) {
    values = values.copy(error = e, loading = false)
  }

}
