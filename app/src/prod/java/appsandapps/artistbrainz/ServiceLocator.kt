package appsandapps.artistbrainz

import android.content.Context
import appsandapps.artistbrainz.datasources.ArtistsDatasourceGraphQL
import appsandapps.artistbrainz.datasources.createBookmarksDatastore
import appsandapps.artistbrainz.repositories.ArtistsRepository
import appsandapps.artistbrainz.repositories.ArtistsRepositoryRemote
import appsandapps.artistbrainz.utils.BooksmarksDatabaseFactory

object ServiceLocator {

  lateinit var artistsRepo: ArtistsRepository

  fun init(context: Context) {
    val db = createBookmarksDatastore(createDatabase(context))
    val graphql = ArtistsDatasourceGraphQL({ db.get(it) != null })
    artistsRepo = ArtistsRepositoryRemote(
      graphql,
      db,
    )
  }

  private fun createDatabase(context: Context) =
    BooksmarksDatabaseFactory(context).createDatabase()

}