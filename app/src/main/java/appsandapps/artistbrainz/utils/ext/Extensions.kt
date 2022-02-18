package appsandapps.artistbrainz.utils.ext

fun String.pluralise(num: Int):String =
  if(num == 1) "1 ${this}" else "${num} ${this}s"


