package appsandapps.artistbrainz

import android.content.Context
import appsandapps.artistbrainz.swapcard.repositories.ArtistsRepositoryInMemory

object ServiceLocator {
  fun provideArtistsRepository(context: Context) =
    ArtistsRepositoryInMemory()
}
