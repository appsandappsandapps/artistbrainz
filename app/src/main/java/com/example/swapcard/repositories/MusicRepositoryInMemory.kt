package com.example.swapcard.repositories

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class MusicRepositoryInMemory : MusicRepository {

  private val _textItems =
    listOf(
      Artist("1", "Aphex Twin"),
      Artist("2", "Neil Young"),
      Artist("3", "Nina Simone"),
    )

  private var lastSearch = ""
  private var searchOffset = 15

  override val searchedForArtists = MutableStateFlow(Artists(_textItems))
  override val bookmarks = MutableStateFlow(Bookmarks(listOf()))

  override suspend fun search(query: String) {
    lastSearch = query
    val newArtists = if(true) {
      searchedForArtists.value.artists.toMutableList().apply {
        add(Artist("999", "1 more"))
        add(Artist("999", "2 more"))
        add(Artist("999", "3 more"))
      }
    } else {
      searchedForArtists.value.artists
    }
    searchedForArtists.value = Artists(newArtists)
  }

  override suspend fun paginateLastSearch(): Unit {
    search(lastSearch)
  }

  override suspend fun bookmark(id: String, name: String) {
    val newArtists = searchedForArtists.value.artists.toMutableList().apply {
      forEachIndexed { index, item ->
        if (item.id == id) set(index, item.copy(bookmarked = true))
      }
    }
    searchedForArtists.value = Artists(newArtists)
  }

  override suspend fun debookmark(id: String) {
    val newArtists = searchedForArtists.value.artists.toMutableList().apply {
      forEachIndexed { index, item ->
        if (item.id == id) set(index, item.copy(bookmarked = false))
      }
    }
    searchedForArtists.value = Artists(newArtists)
  }

  override val artist: StateFlow<Artist> get() = MutableStateFlow(Artist("", ""))

  override suspend fun artist(id: String) {
  }

  override suspend fun clearSearch() {
  }

  override suspend fun isBookmarked(id: String): Boolean {
    return false
  }

}
