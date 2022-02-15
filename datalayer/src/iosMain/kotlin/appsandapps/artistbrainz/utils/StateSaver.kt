package appsandapps.artistbrainz.utils

actual class StateSaver() {

  actual fun save(obj: Any) {}

  actual fun <T> get(default: T): T {
    return default
  }

}