package com.example.architecturetest

import android.content.Context
import com.example.architecturetest.repositories.TextRepositoryInMemory

object ServiceLocator {
  fun provideTextRepository(context: Context) =
    TextRepositoryInMemory()
}