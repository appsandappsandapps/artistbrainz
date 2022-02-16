package appsandapps.artistbrainz.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.*

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
  val resultData: Long = Date().time,
) : Parcelable