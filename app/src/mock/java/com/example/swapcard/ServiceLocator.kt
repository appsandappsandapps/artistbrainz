package com.example.swapcard

import android.content.Context
import com.example.swapcard.repositories.MusicRepositoryInMemory

object ServiceLocator {
  fun provideTextRepository(context: Context) =
    MusicRepositoryInMemory()
}