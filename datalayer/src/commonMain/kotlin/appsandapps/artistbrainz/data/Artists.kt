package appsandapps.artistbrainz.data

import java.util.*

data class Artists(
  val artists: List<Artist> = listOf(),
  val error: String = "",
  val paginated: Boolean = false,
  val resultData: Long = Date().time,
)