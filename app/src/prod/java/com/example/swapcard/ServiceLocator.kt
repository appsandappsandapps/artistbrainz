package com.example.swapcard

import android.content.Context
import com.apollographql.apollo3.ApolloClient
import com.example.swapcard.data.GraphQLDataSource
import com.example.swapcard.data.createBookmarksDatabase
import com.example.swapcard.repositories.MusicRepository
import com.example.swapcard.repositories.MusicRepositoryRemote

object ServiceLocator {

  fun provideMusicRepository(
    context: Context
  ): MusicRepository {
    val db = createBookmarksDatabase(context)
    val graphql = GraphQLDataSource({ db.get(it) != null })
    return MusicRepositoryRemote(
      graphql,
      db,
    )
  }

}