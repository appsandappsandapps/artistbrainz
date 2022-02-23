package appsandapps.artistbrainz.data

import appsandapps.artistbrainz.utils.Parcelable
import appsandapps.artistbrainz.utils.Parcelize

@Parcelize data class Rating (
  val value: Double = 0.0,
  val voteCount: Int = 0,
): Parcelable