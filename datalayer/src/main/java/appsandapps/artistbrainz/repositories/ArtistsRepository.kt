package appsandapps.artistbrainz.repositories

import appsandapps.artistbrainz.data.Artist
import appsandapps.artistbrainz.data.Artists
import appsandapps.artistbrainz.data.Bookmarks
import kotlinx.coroutines.flow.StateFlow

interface ArtistsRepository {
  val searchedForArtists: StateFlow<Artists>
  val bookmarks: StateFlow<Bookmarks>
  val artist: StateFlow<Artist>
  suspend fun search(query: String)
  suspend fun artist(id: String)
  suspend fun paginateLastSearch(): Unit
  suspend fun bookmark(id: String, name: String): Unit
  suspend fun debookmark(id: String): Unit
}

