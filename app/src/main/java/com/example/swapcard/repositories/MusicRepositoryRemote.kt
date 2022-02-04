package com.example.swapcard.repositories

import android.util.Log
import com.apollographql.apollo3.ApolloClient
import com.example.swapcard.ArtistQuery
import com.example.swapcard.ArtistsQuery
import kotlinx.coroutines.flow.MutableStateFlow

class MusicRepositoryRemote(
  val apolloClient: ApolloClient
): MusicRepository {

  private var lastQuery = ""
  private var lastCursorResult = ""
  private var activeBottomCursor = ""

  override val searchedForArtists = MutableStateFlow(Artists(listOf()))
  override val artist = MutableStateFlow(Artist("", ""))

  override suspend fun clearSearch() {
    activeBottomCursor = ""
    lastCursorResult = ""
    searchedForArtists.value = Artists(listOf())
  }

  override suspend fun search(query: String) {
    if(query != lastQuery) activeBottomCursor = ""
    lastQuery = query
    try {
      val resp = apolloClient.query(ArtistsQuery(
        query, activeBottomCursor
      )).execute()
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
          )
        }
      searchedForArtists.value = Artists(artists!!)
    } catch(e: Exception) {
      Log.d("HI", "error!" + e)
    }
  }

  override suspend fun refresh() {
    searchedForArtists.value = Artists(searchedForArtists.value.artists)
  }

  override suspend fun paginateLastSearch() {
    activeBottomCursor = lastCursorResult
    search(lastQuery)
  }

  override suspend fun bookmark(id: String) {
  }

  override suspend fun debookmark(id: String) {
  }

  override suspend fun artist(id: String) {
    try {
      val resp = apolloClient.query(ArtistQuery(id)).execute()
      val artistFrag = resp.data?.node?.artistDetailsFragment!!
      var art = Artist(
        id = artistFrag.id,
        name = artistFrag.name ?: "",
        disambiguation = artistFrag.disambiguation ?: "",
      )
      artist.value = art
    } catch(e: Exception) {
      Log.d("HI", "error!" + e)
    }
  }

}