package appsandapps.artistbrainz

import appsandapps.artistbrainz.datasources.ArtistsDatasourceGraphQL
import appsandapps.artistbrainz.datasources.BookmarksDatastoreSqlDelight
import appsandapps.artistbrainz.repositories.ArtistsRepository
import appsandapps.artistbrainz.repositories.ArtistsRepositoryRemote
import appsandapps.artistbrainz.utils.BookmarksDatabaseFactory

actual class ServiceLocator() {

  init {
    val db = BookmarksDatabaseFactory().createDatabase()
    val datastore = BookmarksDatastoreSqlDelight(db)
    val graphql = ArtistsDatasourceGraphQL({ datastore.get(it) != null })
    artistsRepo = ArtistsRepositoryRemote(
      graphql,
      datastore,
    )
  }

  companion actual object {
    actual var artistsRepo: ArtistsRepository? = null
  }

}