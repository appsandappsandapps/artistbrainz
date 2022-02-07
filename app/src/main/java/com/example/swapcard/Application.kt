package com.example.swapcard

import android.app.Application
import com.example.swapcard.repositories.ArtistsRepository

// Links to youtube
// Links to discogs

// Upload to Google play
// Start a PDF

// Images

class Application : Application() {

  // Depends on the build flavour's ServiceLocator
  lateinit var artistsRepository: ArtistsRepository
    private set

  override fun onCreate() {
    super.onCreate()
    artistsRepository = ServiceLocator.provideArtistsRepository(this)
  }

}