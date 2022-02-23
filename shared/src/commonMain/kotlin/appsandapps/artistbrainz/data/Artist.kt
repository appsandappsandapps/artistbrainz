package appsandapps.artistbrainz.data

import appsandapps.artistbrainz.utils.Parcelable
import appsandapps.artistbrainz.utils.Parcelize
import kotlinx.datetime.Clock

@Parcelize data class Artist (
  val id: String = "",
  val name: String = "",
  val bookmarked: Boolean = false,
  val disambiguation: String = "",
  val summary: String = "",
  val lastFMUrl: String = "",
  val imageUrl: String = "",
  val rating: Rating = Rating(),
  val error: String = "",
  val resultData: Long = Clock.System.now().epochSeconds
) : Parcelable