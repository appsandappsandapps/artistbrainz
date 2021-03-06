package appsandapps.artistbrainz.datasources

import com.apollographql.apollo3.ApolloClient
import appsandapps.artistbrainz.ArtistQuery
import appsandapps.artistbrainz.ArtistsQuery
import appsandapps.artistbrainz.data.Artist
import appsandapps.artistbrainz.data.Rating
import appsandapps.artistbrainz.repositories.ArtistsDatasource

class ArtistsDatasourceGraphQL(
  val isBookmarked: suspend (String) -> Boolean,
) : ArtistsDatasource {

  private val apolloClient = ApolloClient.Builder()
    .serverUrl("https://graphbrainz.herokuapp.com/")
    .build()

  override suspend fun getArtist(id: String): Artist {
    val resp = apolloClient.query(ArtistQuery(id)).execute()
    val artistFrag = resp.data?.node?.artistDetailsFragment!!
    val image = artistFrag.discogs?.images?.let {
      if(it.size == 0) ""
      else it.get(0).url.toString()
    } ?: ""
    var lastFmUrl = artistFrag.lastFM?.url
    if(lastFmUrl == null) lastFmUrl = ""
    else lastFmUrl = lastFmUrl.toString()
    return Artist(
      id = artistFrag.id,
      name = artistFrag.name ?: "",
      disambiguation = artistFrag.disambiguation ?: "",
      summary = artistFrag.lastFM?.biography?.summaryHTML ?: "",
      lastFMUrl = lastFmUrl,
      imageUrl = image,
      rating = Rating(
        value = artistFrag.rating?.value ?: 0.0,
        voteCount = artistFrag.rating?.voteCount ?: 0,
      )
    )
  }

  override suspend fun getArtists(
    query: String,
    lastCursor: String,
  ): Pair<List<Artist>, String> {
    var lastCursorResult = ""
    val resp = apolloClient.query(
      ArtistsQuery(query, lastCursor)
    ).execute()
    val artists = resp.data?.search?.artists?.edges
      ?.filterNotNull()
      ?.map {
        Pair(it.node?.artistBasicFragment, it.cursor)
      }
      ?.map {
        val artist = it.first!!
        lastCursorResult = it.second
        Artist(
          id = artist.id,
          name = artist.name ?: "",
          disambiguation = artist.disambiguation ?: "",
          bookmarked = isBookmarked(artist.id),
        )
      }
    return Pair(artists!!, lastCursorResult)
  }

}