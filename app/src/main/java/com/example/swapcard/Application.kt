package com.example.swapcard

import android.app.Application
import com.example.swapcard.repositories.MusicRepository

// Loading spinner?
// Live datasource

class Application : Application() {

  // Depends on ServiceLocator in build flavour
  lateinit var musicRepository: MusicRepository
    private set

  override fun onCreate() {
    super.onCreate()
    musicRepository = ServiceLocator.provideTextRepository(this)
  }

}