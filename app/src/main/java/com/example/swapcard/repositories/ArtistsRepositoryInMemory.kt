package com.example.swapcard.repositories

import com.example.swapcard.data.Artist
import com.example.swapcard.data.Artists
import com.example.swapcard.data.Bookmark
import com.example.swapcard.data.Bookmarks
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.util.*

class ArtistsRepositoryInMemory : ArtistsRepository {

  private val _textItems =
    listOf(
      Artist("1", "Aphex Twin"),
      Artist("2", "Neil Young"),
      Artist("3", "Nina Simone"),
    )

  override val artist: StateFlow<Artist> get() = MutableStateFlow(Artist("Dummy Id", "Dummy Artist"))
  override val searchedForArtists = MutableStateFlow(Artists(_textItems))
  override val bookmarks = MutableStateFlow(Bookmarks(listOf()))

  override suspend fun search(query: String) {
    searchedForArtists.value = Artists(searchedForArtists.value.artists)
  }

  override suspend fun paginateLastSearch(): Unit {
    val newArtists = mutableListOf(
      Artist("${Date().time}1", "1 more"),
      Artist("${Date().time}2", "2 more"),
      Artist("${Date().time}3", "3 more"),
    )
    searchedForArtists.value = Artists(newArtists)
  }

  override suspend fun bookmark(id: String, name: String) {
    bookmarks.value = Bookmarks(
      bookmarks.value.bookmarks.toMutableList().apply {
        add(Bookmark(id, name))
      }
    )
  }

  override suspend fun debookmark(id: String) {
    var bms = bookmarks.value.bookmarks.filter { it.id != id }
    bookmarks.value = Bookmarks(bms)
  }

  override suspend fun artist(id: String) {}

}
