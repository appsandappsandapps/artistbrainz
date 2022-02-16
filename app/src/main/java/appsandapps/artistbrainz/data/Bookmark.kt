package appsandapps.artistbrainz.data

import appsandapps.artistbrainz.utils.Parcelable
import appsandapps.artistbrainz.utils.Parcelize

@Parcelize data class Bookmark (
  val id: String = "",
  val name: String = "",
) : Parcelable