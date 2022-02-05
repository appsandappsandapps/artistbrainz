package com.example.swapcard.repositories

import kotlinx.coroutines.flow.StateFlow
import java.util.*

data class Rating (
  val value: Int = 0,
  val voteCount: Int = 0,
)

data class Bookmark (
  val id: String = "",
  val name: String = "",
)

data class Bookmarks (
  val bookmarks: List<Bookmark>,
)

data class Artist (
  val id: String,
  val name: String,
  val bookmarked: Boolean = false,
  val disambiguation: String = "...HMMM",
  val rating: Rating = Rating(),
)

data class Artists(
  val artists: List<Artist> = listOf(),
  val error: String = "",
  val paginated: Boolean = false,
  val resultData: Long = Date().time,
)

interface MusicRepository {
  val searchedForArtists: StateFlow<Artists>
  val bookmarks: StateFlow<Bookmarks>
  val artist: StateFlow<Artist>
  suspend fun search(query: String)
  suspend fun clearSearch()
  suspend fun artist(id: String)
  suspend fun paginateLastSearch(): Unit
  suspend fun bookmark(id: String, name: String): Unit
  suspend fun isBookmarked(id: String): Boolean
  suspend fun debookmark(id: String): Unit
}

