package appsandapps.artistbrainz.repositories

import appsandapps.artistbrainz.data.*
import appsandapps.artistbrainz.utils.IODispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

interface ArtistsDatasource {
  suspend fun getArtists(query: String, lastCursor: String): Pair<List<Artist>, String>
  suspend fun getArtist(id: String): Artist
}

interface BookmarksDatasource {
  suspend fun getAll(): List<Bookmark>
  suspend fun get(id: String): Bookmark?
  suspend fun delete(id: String)
  suspend fun insert(id: String, name: String)
}

/**
 * Merges a bookmarks database
 * with the graphql datasource
 */
class ArtistsRepositoryRemote(
  private val artistsDatasouce: ArtistsDatasource,
  private val bookmarksDatasouce: BookmarksDatasource,
  private val dispatcher: CoroutineDispatcher = IODispatcher
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
      val (artists, lastCursor) = artistsDatasouce.getArtists(query, activeCursor)
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
      val newArtist = artistsDatasouce
        .getArtist(id)
        .copy(bookmarked = isBookmarked(id))
      artist.value = newArtist
    } catch(e: Exception) {
      artist.value = Artist("", "", error = "Error fetching artist: ${e.message}")
    }
  }

  override suspend fun bookmark(id: String, name: String) {
    try {
      bookmarksDatasouce.insert(id, name)
      artist(artist.value.id)
      refreshBookmarks()
    } catch (e: Exception) {
      // TODO: The repo should have a generic error stateflow
      // since atm errors are localised to artists and artist
      // stateflows and we're unsure which one to do
    }
  }

  override suspend fun debookmark(id: String) {
    try {
      bookmarksDatasouce.delete(id)
      artist(artist.value.id)
      refreshBookmarks()
    } catch(e: Exception) {
      // TODO: The repo should have a generic error stateflow
      // since atm errors are localised to artists and artist
      // stateflows and we're unsure which one to do
    }
  }

  private suspend fun isBookmarked(id: String) =
    bookmarksDatasouce.get(id) != null

  private suspend fun refreshBookmarks() {
    val bookmarksEntities = bookmarksDatasouce.getAll()
    bookmarks.value = Bookmarks(
      bookmarksEntities.map { Bookmark(it.id, it.name) }
    )
  }

}