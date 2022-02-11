package appsandapps.artistbrainz

import android.content.Context
import appsandapps.artistbrainz.data.datasources.ArtistsDataSourceGraphQL
import appsandapps.artistbrainz.data.datasources.BookmarksDatasourceRoomDb
import appsandapps.artistbrainz.repositories.ArtistsRepository
import appsandapps.artistbrainz.repositories.ArtistsRepositoryRemote

object ServiceLocator {

  fun provideArtistsRepository(
    context: Context
  ): ArtistsRepository {
    val db = BookmarksDatasourceRoomDb(context)
    val graphql = ArtistsDataSourceGraphQL({ db.get(it) != null })
    return ArtistsRepositoryRemote(
      graphql,
      db,
    )
  }

}