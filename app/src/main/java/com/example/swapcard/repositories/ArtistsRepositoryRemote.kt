package com.example.swapcard.repositories

import com.example.swapcard.data.*
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

/**
 * Merges a bookmarks database
 * with the graphql datasource
 */
class ArtistsRepositoryRemote(
  private val graphQLDatasource: GraphQLDataSource,
  private val bookmarksDAO: BookmarkDao,
  private val dispatcher: CoroutineDispatcher = Dispatchers.IO
): ArtistsRepository {

  private var lastQuery = ""
  private var lastCursorResult = ""
  private var activeCursor = ""

  override val searchedForArtists = MutableStateFlow(Artists(listOf(), ""))
  override val artist = MutableStateFlow(Artist("", ""))
  override val bookmarks = MutableStateFlow(Bookmarks(listOf()))

  init {
    MainScope().launch(dispatcher) {
      refreshBookmarks()
    }
  }

  override suspend fun search(query: String) {
    activeCursor = ""
    lastCursorResult = ""
    searchRaw(query)
  }

  override suspend fun paginateLastSearch() {
    activeCursor = lastCursorResult
    searchRaw(lastQuery)
  }

  private suspend fun searchRaw(query: String) {
    if(query != lastQuery) activeCursor = ""
    lastQuery = query
    try {
      val (artists, lastCursor) = graphQLDatasource.getArtists(query, activeCursor)
      lastCursorResult = lastCursor
      searchedForArtists.value = Artists(
        artists,
        paginated = activeCursor.isNotBlank(),
      )
    } catch(e: Exception) {
      searchedForArtists.value = Artists(
        error = "Error fetching artists: ${e}"
      )
    }
  }

  override suspend fun artist(id: String) {
    try {
      val newArtist = graphQLDatasource
        .getArtist(id)
        .copy(bookmarked = isBookmarked(id))
      artist.value = newArtist
    } catch(e: Exception) {
      artist.value = Artist("", "", error = "Error fetching artist: ${e.message}")
    }
  }

  override suspend fun bookmark(id: String, name: String) {
    bookmarksDAO.insert(BookmarkEntity(id, name))
    refreshBookmarks()
  }

  override suspend fun debookmark(id: String) {
    bookmarksDAO.delete(id)
    refreshBookmarks()
  }

  private suspend fun isBookmarked(id: String) =
    bookmarksDAO.get(id) != null

  private suspend fun refreshBookmarks() {
    val bookmarksEntities = bookmarksDAO.getAll()
    bookmarks.value = Bookmarks(
      bookmarksEntities.map { Bookmark(it.id, it.name) }
    )
  }

}