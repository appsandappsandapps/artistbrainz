package com.example.architecturetest

import android.content.Context
import com.example.architecturetest.repositories.TextRepositoryDatabase
import com.example.architecturetest.repositories.TextRepositoryRemote

object ServiceLocator {
  fun provideTextRepository(context: Context) =
    TextRepositoryDatabase(context)
    //TextRepositoryRemote(context)
}