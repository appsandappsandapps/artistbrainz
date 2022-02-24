package appsandapps.artistbrainz.repositories

import appsandapps.artistbrainz.data.Artist
import appsandapps.artistbrainz.data.Artists
import appsandapps.artistbrainz.data.Bookmark
import appsandapps.artistbrainz.data.Bookmarks
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.datetime.Clock

class ArtistsRepositoryInMemory : ArtistsRepository {

  private val _textItems =
    listOf(
      Artist("1", "Aphex Twin"),
      Artist("2", "Neil Young"),
      Artist("3", "Nina Simone"),
    )

  override val artist: StateFlow<Artist> get() = MutableStateFlow(Artist("Dummy Id", "Dummy Artist"))
  override val searchedForArtists = MutableStateFlow(Artists())
  override val bookmarks = MutableStateFlow(Bookmarks(listOf()))

  override suspend fun search(query: String) {
    searchedForArtists.value = Artists(_textItems)
  }

  override suspend fun paginateLastSearch(): Unit {
    val newArtists = mutableListOf(
      Artist("${Clock.System.now()}1", "1 more"),
      Artist("${Clock.System.now()}2", "2 more"),
      Artist("${Clock.System.now()}3", "3 more"),
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
