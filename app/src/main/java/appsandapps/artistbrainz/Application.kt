package appsandapps.artistbrainz

import android.app.Application
import appsandapps.artistbrainz.repositories.ArtistsRepository

// Images
// Order by bookmark entry date
// Generic error in repository, rather than in artists/artist

class Application : Application() {

  override fun onCreate() {
    super.onCreate()
    // Depends on the build flavour's ServiceLocator
    ServiceLocator.init(this)
  }

}