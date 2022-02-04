package com.example.swapcard.repositories

import android.content.Context
import android.util.Log
import com.apollographql.apollo3.ApolloClient
import com.example.swapcard.ArtistsQuery
import kotlinx.coroutines.flow.MutableStateFlow

/*

/**
 * This doesn't add anything as yet...
 * It's just to test using retofit and okhttp
 */
class MusicRepositoryRemote(context: Context): MusicRepository {

  override val artists = MutableStateFlow<ArtistList>(
    ArtistList(listOf(), 0)
  )

  val apolloClient = ApolloClient.Builder()
    .serverUrl("https://graphbrainz.herokuapp.com/")
    .build()

  override suspend fun search(text: String, offset: Int) {
    refresh()
  }

  override suspend fun refresh() {
    val resp = apolloClient.query(ArtistsQuery("Aphex Twin")).execute()
    val artists = resp.data?.search?.artists?.nodes
      ?.filter {
        it != null && it.artistBasicFragment != null
      }
      ?.map {
        it?.artistBasicFragment
      }
    try {
      //allText.value = file.map { "${it.number}" }
    } catch(e: Exception) {
      Log.d("HI", "error!")
    }
  }

  override suspend fun paginateLastSearch() {
  }

  override suspend fun bookmark(id: Int) {
  }

  override suspend fun debookmark(id: Int) {
  }

}

 */