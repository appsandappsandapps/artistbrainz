package appsandapps.artistbrainz

import android.content.Context
import appsandapps.artistbrainz.datasources.ArtistsDatasourceGraphQL
import appsandapps.artistbrainz.datasources.createBookmarksDatastore
import appsandapps.artistbrainz.repositories.ArtistsRepository
import appsandapps.artistbrainz.repositories.ArtistsRepositoryRemote
import appsandapps.artistbrainz.utils.BooksmarksDatabaseFactory

object ServiceLocator {

  fun provideArtistsRepository(
    context: Context
  ): ArtistsRepository {
    val db = createBookmarksDatastore(createDatabase(context))
    val graphql = ArtistsDatasourceGraphQL({ db.get(it) != null })
    return ArtistsRepositoryRemote(
      graphql,
      db,
    )
  }

  private fun createDatabase(context: Context) =
    BooksmarksDatabaseFactory(context).createDatabase()

}