package appsandapps.artistbrainz.utils

actual class StateSaver() : StateSaveable {

  override fun save(obj: Parcelable): Unit {}

  override fun <T : Parcelable> get(default : T): T = default

}