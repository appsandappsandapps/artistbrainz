package com.example.swapcard

import android.content.Context
import com.apollographql.apollo3.ApolloClient
import com.example.swapcard.repositories.MusicRepositoryRemote

object ServiceLocator {


  fun provideMusicRepository(context: Context) =
    MusicRepositoryRemote(apolloClient)

  private val apolloClient = ApolloClient.Builder()
    .serverUrl("https://graphbrainz.herokuapp.com/")
    .build()
}