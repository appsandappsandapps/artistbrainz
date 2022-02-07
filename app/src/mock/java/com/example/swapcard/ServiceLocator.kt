package com.example.swapcard

import android.content.Context
import com.example.swapcard.repositories.ArtistsRepositoryInMemory

object ServiceLocator {
  fun provideArtistsRepository(context: Context) =
    ArtistsRepositoryInMemory()
}
