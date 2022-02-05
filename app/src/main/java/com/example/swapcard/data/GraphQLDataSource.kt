package com.example.swapcard.data

import com.apollographql.apollo3.ApolloClient
import com.example.swapcard.ArtistQuery
import com.example.swapcard.ArtistsQuery
import com.example.swapcard.repositories.Artist

class GraphQLDataSource(
  val isBookmarked: suspend (String) -> Boolean,
) {

  val apolloClient = ApolloClient.Builder()
    .serverUrl("https://graphbrainz.herokuapp.com/")
    .build()

  suspend fun getArtist(id: String): Artist {
    val resp = apolloClient.query(ArtistQuery(id)).execute()
    val artistFrag = resp.data?.node?.artistDetailsFragment!!
    return Artist(
      id = artistFrag.id,
      name = artistFrag.name ?: "",
      disambiguation = artistFrag.disambiguation ?: "",
    )
  }

  suspend fun getArtists(
    query: String,
    cursor: String,
  ): Pair<List<Artist>, String> {
    var lastCursorResult = ""
    val resp = apolloClient.query(
      ArtistsQuery(query, cursor)
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