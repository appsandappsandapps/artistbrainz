package appsandapps.artistbrainz.utils

interface StateSaveable {
  fun save(obj: Parcelable): Unit
  fun <T : Parcelable> get(defaultObj: T): T
}

class StateSaverEmpty : StateSaveable {
  override fun save(obj: Parcelable) { }
  override fun <T : Parcelable> get(defaultObj: T): T = defaultObj
}

expect class StateSaver : StateSaveable
