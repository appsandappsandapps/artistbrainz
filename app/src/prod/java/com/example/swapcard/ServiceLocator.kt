package com.example.swapcard

import android.content.Context
import com.example.swapcard.data.GraphQLDataSource
import com.example.swapcard.data.createBookmarksDatabase
import com.example.swapcard.repositories.ArtistsRepository
import com.example.swapcard.repositories.ArtistsRepositoryRemote

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