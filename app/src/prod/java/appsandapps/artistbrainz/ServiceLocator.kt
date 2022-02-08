package appsandapps.artistbrainz

import android.content.Context
import appsandapps.artistbrainz.data.GraphQLDataSource
import appsandapps.artistbrainz.data.createBookmarksDatabase
import appsandapps.artistbrainz.repositories.ArtistsRepository
import appsandapps.artistbrainz.repositories.ArtistsRepositoryRemote

object ServiceLocator {

  fun provideArtistsRepository(
    context: Context
  ): ArtistsRepository {
    val db = createBookmarksDatabase(context)
    val graphql = GraphQLDataSource({ db.get(it) != null })
    return ArtistsRepositoryRemote(
      graphql,
      db,
    )
  }

}