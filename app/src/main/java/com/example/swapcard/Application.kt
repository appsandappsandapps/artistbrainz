package com.example.swapcard

import android.app.Application
import com.example.swapcard.repositories.MusicRepository

// Bookmarks again
// Loading spinner?
// Error spinner?
// No results

class Application : Application() {

  // Depends on ServiceLocator in build flavour
  lateinit var musicRepository: MusicRepository
    private set

  override fun onCreate() {
    super.onCreate()
    musicRepository = ServiceLocator.provideMusicRepository(this)
  }

}