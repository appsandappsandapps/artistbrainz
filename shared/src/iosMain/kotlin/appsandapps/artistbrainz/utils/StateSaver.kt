package appsandapps.artistbrainz.utils

actual class StateSaver() : StateSaveable {

  override fun save(obj: Parcelable): Unit {}

  override fun <T : Parcelable> get(defaultObj: T): T = defaultObj

}