package com.example.architecturetest.repositories

import android.content.Context
import com.example.architecturetest.data.Thing
import com.example.architecturetest.data.createDatabase
import kotlinx.coroutines.flow.MutableStateFlow

class TextRepositoryDatabase(context: Context): TextRepository {

  val db = createDatabase(context)
  override val allText = MutableStateFlow<List<String>>(listOf())

  override suspend fun addText(text: String) {
    db.insert(Thing(thing = text))
    refresh()
  }

  override suspend fun refresh() {
    allText.value = db.getAll().map { it.thing }
  }

}
