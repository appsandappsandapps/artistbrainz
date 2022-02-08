package appsandapps.artistbrainz.repositories

import appsandapps.artistbrainz.data.Artist
import appsandapps.artistbrainz.data.BookmarkDao
import appsandapps.artistbrainz.data.GraphQLDataSource
import appsandapps.artistbrainz.utils.launchAndWait
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
@ExperimentalCoroutinesApi
class ArtistsRepositoryRemoteTests {

  @Before fun setUp() = Dispatchers.setMain(StandardTestDispatcher())
  @After fun tearDown() = Dispatchers.resetMain()

  @Mock lateinit var remote: GraphQLDataSource
  @Mock lateinit var bookmarkDao: BookmarkDao

  @Test
  fun `repo checks bookmark status for artist`() = runTest {
    val artistId = "0"
    `when`(bookmarkDao.getAll()).thenReturn(listOf())
    val repo = ArtistsRepositoryRemote(remote, bookmarkDao)
    launchAndWait { repo.artist(artistId) }

    verify(bookmarkDao, times(1)).get(artistId)
  }

  @Test
  fun `repo gets bookmarks on init`() = runTest {
    val artistId = "1"
    `when`(bookmarkDao.getAll()).thenReturn(listOf())
    launchAndWait {
      ArtistsRepositoryRemote(
        remote,
        bookmarkDao,
        Dispatchers.Main,
      )
    }

    verify(bookmarkDao, times(1)).getAll()
  }

  @Test
  fun `repo calls remote artists on search`() = runTest {
    `when`(bookmarkDao.getAll()).thenReturn(listOf())
    `when`(remote.getArtists("", "")).thenReturn(Pair(listOf(
      Artist("")
    ), ""))
    launchAndWait { ArtistsRepositoryRemote(remote, bookmarkDao).search("") }

    verify(remote, times(1)).getArtists("", "")
  }

}