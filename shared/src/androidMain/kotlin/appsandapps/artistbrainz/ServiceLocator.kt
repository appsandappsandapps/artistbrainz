package appsandapps.artistbrainz

import android.content.Context
import appsandapps.artistbrainz.datasources.ArtistsDatasourceGraphQL
import appsandapps.artistbrainz.datasources.BookmarksDatastoreSqlDelight
import appsandapps.artistbrainz.repositories.ArtistsRepository
import appsandapps.artistbrainz.repositories.ArtistsRepositoryRemote
import appsandapps.artistbrainz.utils.BookmarksDatabaseFactory

actual class ServiceLocator(context: Context) {

  init {
    val db = BookmarksDatabaseFactory(context).createDatabase()
    val datasource = BookmarksDatastoreSqlDelight(db)
    val graphql = ArtistsDatasourceGraphQL({ datasource.get(it) != null })
    artistsRepo = ArtistsRepositoryRemote(
      graphql,
      datasource,
    )
  }

  companion actual object {
    actual var artistsRepo: ArtistsRepository? = null
  }

}