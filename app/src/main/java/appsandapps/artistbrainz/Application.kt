package appsandapps.artistbrainz

import android.app.Application
import appsandapps.artistbrainz.repositories.ArtistsRepository

// Tests now actions in uistate

// Images
// Order by bookmark entry date

class Application : Application() {

  // Depends on the build flavour's ServiceLocator
  lateinit var artistsRepository: ArtistsRepository
    private set

  override fun onCreate() {
    super.onCreate()
    artistsRepository = ServiceLocator.provideArtistsRepository(this)
  }

}