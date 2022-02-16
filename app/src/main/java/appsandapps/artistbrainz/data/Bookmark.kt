package appsandapps.artistbrainz.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize data class Bookmark (
  val id: String = "",
  val name: String = "",
) : Parcelable