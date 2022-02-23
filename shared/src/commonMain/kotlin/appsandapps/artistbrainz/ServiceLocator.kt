package appsandapps.artistbrainz

import appsandapps.artistbrainz.repositories.ArtistsRepository

expect class ServiceLocator {
  companion object {
    var artistsRepo: ArtistsRepository?
  }
}