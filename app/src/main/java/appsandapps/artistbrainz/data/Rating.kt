package appsandapps.artistbrainz.data

import android.os.Parcel
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize data class Rating (
  val value: Double = 0.0,
  val voteCount: Int = 0,
): Parcelable