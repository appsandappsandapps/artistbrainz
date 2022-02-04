package com.example.architecturetest.repositories

import kotlinx.coroutines.flow.StateFlow

interface TextRepository {
  val allText: StateFlow<List<String>>
  suspend fun refresh()
  suspend fun addText(text: String)
}

