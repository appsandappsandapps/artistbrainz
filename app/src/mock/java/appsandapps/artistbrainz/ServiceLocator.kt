package appsandapps.artistbrainz

import android.content.Context
import appsandapps.artistbrainz.repositories.ArtistsRepositoryInMemory

object ServiceLocator {
  fun provideArtistsRepository(context: Context) =
    ArtistsRepositoryInMemory()
}
