package com.example.swapcard.repositories

import kotlinx.coroutines.flow.MutableStateFlow

class MusicRepositoryInMemory : MusicRepository {

  private val _textItems =
    listOf(
      Artist(1, "Aphex Twin"),
      Artist(2, "Neil Young"),
      Artist(3, "Nina Simone"),
    )

  private var lastSearch = ""
  private var searchOffset = 15

  override val searchedForArtists = MutableStateFlow(Artists(_textItems))

  override suspend fun search(text: String, offset: Int) {
    lastSearch = text
    val newArtists = if(offset > 0) {
      searchedForArtists.value.artists.toMutableList().apply {
        add(Artist(999, "1 more"))
        add(Artist(999, "2 more"))
        add(Artist(999, "3 more"))
      }
    } else {
      searchedForArtists.value.artists
    }
    searchedForArtists.value = Artists(newArtists)
  }

  override suspend fun refresh() {
    searchedForArtists.value = Artists(searchedForArtists.value.artists)
  }

  override suspend fun paginateLastSearch(): Unit {
    searchOffset += 15
    search(lastSearch, searchOffset)
  }

  override suspend fun bookmark(id: Int) {
    val newArtists = searchedForArtists.value.artists.toMutableList().apply {
      forEachIndexed { index, item ->
        if (item.id == id) set(index, item.copy(bookmarked = true))
      }
    }
    searchedForArtists.value = Artists(newArtists)
  }

  override suspend fun debookmark(id: Int) {
    val newArtists = searchedForArtists.value.artists.toMutableList().apply {
      forEachIndexed { index, item ->
        if (item.id == id) set(index, item.copy(bookmarked = false))
      }
    }
    searchedForArtists.value = Artists(newArtists)
  }

}
