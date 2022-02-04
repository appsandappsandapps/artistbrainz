package com.example.swapcard.repositories

import kotlinx.coroutines.flow.StateFlow

data class Rating (
  val value: Int = 10,
  val voteCount: Int = 20,
)

data class Artist (
  val id: Int,
  val name: String,
  val bookmarked: Boolean = false,
  val disambiguation: String = "...HMMM",
  val rating: Rating = Rating()
)

data class Artists(
  val artists: List<Artist> = listOf()
)

interface MusicRepository {
  val searchedForArtists: StateFlow<Artists>
  suspend fun refresh()
  suspend fun search(text: String, offset: Int = 0)
  suspend fun paginateLastSearch(): Unit
  suspend fun bookmark(id: Int): Unit
  suspend fun debookmark(id: Int): Unit
}

