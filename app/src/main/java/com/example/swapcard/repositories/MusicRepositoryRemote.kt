package com.example.swapcard.repositories

import android.util.Log
import com.example.swapcard.data.BookmarkEntity
import com.example.swapcard.data.BookmarkDao
import com.example.swapcard.data.GraphQLDataSource
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import java.util.*

class MusicRepositoryRemote(
  private val graphQLDatasource: GraphQLDataSource,
  private val bookmarksDAO: BookmarkDao,
): MusicRepository {

  private var lastQuery = ""
  private var lastCursorResult = ""
  private var activeCursor = ""

  override val searchedForArtists = MutableStateFlow(Artists(listOf(), ""))
  override val artist = MutableStateFlow(Artist("", ""))
  override val bookmarks = MutableStateFlow(Bookmarks(listOf()))

  init {
    MainScope().launch {
      refreshBookmarks()
    }
  }

  override suspend fun clearSearch() {
    activeCursor = ""
    lastCursorResult = ""
    searchedForArtists.value = Artists(listOf())
  }

  override suspend fun search(query: String) {
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

  override suspend fun paginateLastSearch() {
    activeCursor = lastCursorResult
    search(lastQuery)
  }

  override suspend fun artist(id: String) {
    try {
      val newArtist = graphQLDatasource.getArtist(id)
      artist.value = newArtist
    } catch(e: Exception) {
      Log.d("HI", "error!" + e)
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

  override suspend fun isBookmarked(id: String) =
    bookmarksDAO.get(id) != null

  private suspend fun refreshBookmarks() {
    val bookmarksEntities = bookmarksDAO.getAll()
    bookmarks.value = Bookmarks(
      bookmarksEntities.map { Bookmark(it.id, it.name) }
    )
  }

}