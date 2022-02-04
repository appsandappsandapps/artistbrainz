package com.example.architecturetest

import android.app.Application
import com.example.architecturetest.repositories.TextRepository

class Application : Application() {

  // Depends on ServiceLocator in build flavour
  lateinit var textRepository: TextRepository
    private set

  override fun onCreate() {
    super.onCreate()
    textRepository = ServiceLocator.provideTextRepository(this)
  }

}