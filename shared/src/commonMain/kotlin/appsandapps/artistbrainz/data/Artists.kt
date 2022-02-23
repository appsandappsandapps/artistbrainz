package appsandapps.artistbrainz.data

import kotlinx.datetime.Clock

data class Artists(
  val artists: List<Artist> = listOf(),
  val error: String = "",
  val paginated: Boolean = false,
  val resultData: Long = Clock.System.now().epochSeconds
)