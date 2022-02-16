package appsandapps.artistbrainz

import android.content.Context
import appsandapps.artistbrainz.datasources.ArtistsDatasourceGraphQL
import appsandapps.artistbrainz.datasources.createBookmarksDatabase
import appsandapps.artistbrainz.repositories.ArtistsRepository
import appsandapps.artistbrainz.repositories.ArtistsRepositoryRemote

object ServiceLocator {

  fun provideArtistsRepository(
    context: Context
  ): ArtistsRepository {
    val db = createBookmarksDatabase(context)
    val graphql = ArtistsDatasourceGraphQL({ db.get(it) != null })
    return ArtistsRepositoryRemote(
      graphql,
      db,
    )
  }

}