package appsandapps.artistbrainz

import android.app.Application
import appsandapps.artistbrainz.repositories.ArtistsRepository

// Images
// Order by bookmark entry date
// Generic error in repository, rather than in artists/artist

class Application : Application() {

  // Depends on the build flavour's ServiceLocator
  lateinit var artistsRepository: ArtistsRepository
    private set

  override fun onCreate() {
    super.onCreate()
    artistsRepository = ServiceLocator.provideArtistsRepository(this)
  }

}