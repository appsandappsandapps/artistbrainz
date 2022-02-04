package com.example.architecturetest.repositories

import kotlinx.coroutines.flow.MutableStateFlow

class TextRepositoryInMemory : TextRepository {

  private val _textItems = mutableListOf<String>()

  override val allText = MutableStateFlow(_textItems.toList())

  override suspend fun addText(text: String) {
    allText.value =
      _textItems.apply { this.add(text+"!") }.toList()
  }

  override suspend fun refresh() {
    _textItems.toList()
  }

}
