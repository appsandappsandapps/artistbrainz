package appsandapps.artistbrainz.utils

interface StateSaveable {
  fun save(obj: Parcelable): Unit
  fun <T : Parcelable> get(default: T): T
}

class StateSaverEmpty : StateSaveable {
  override fun save(obj: Parcelable) { }
  override inline fun <T : Parcelable> get(default: T): T = default
}

expect class StateSaver : StateSaveable
