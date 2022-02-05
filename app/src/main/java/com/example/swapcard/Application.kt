package com.example.swapcard

import android.app.Application
import com.example.swapcard.repositories.MusicRepository

// Rating for artist
// Error for artists
// Loading for artist

// Clear input for search
// Search button

class Application : Application() {

  // Depends on ServiceLocator in build flavour
  lateinit var musicRepository: MusicRepository
    private set

  override fun onCreate() {
    super.onCreate()
    musicRepository = ServiceLocator.provideMusicRepository(this)
  }

}