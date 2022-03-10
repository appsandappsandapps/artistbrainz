package appsandapps.artistbrainz.utils

import appsandapps.artistbrainz.BookmarksDatabase
import appsandapps.artistbrainz.datasources.ArtistsDatasourceGraphQL
import appsandapps.artistbrainz.datasources.BookmarksDatastoreSqlDelight
import appsandapps.artistbrainz.repositories.ArtistsRepository
import appsandapps.artistbrainz.repositories.ArtistsRepositoryRemote
import com.squareup.sqldelight.db.SqlDriver
import com.squareup.sqldelight.drivers.native.NativeSqliteDriver
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Runnable
import platform.darwin.dispatch_async
import platform.darwin.dispatch_get_main_queue
import kotlin.coroutines.CoroutineContext
import kotlin.native.concurrent.ThreadLocal

actual interface Parcelable

actual val IODispatcher : CoroutineDispatcher get() = MainDispatcher //Dispatchers.Default

@ThreadLocal
public object MainDispatcher: CoroutineDispatcher() {
  override fun dispatch(context: CoroutineContext, block: Runnable) {
    dispatch_async(dispatch_get_main_queue()) {
      try {
        block.run()
      } catch (err: Throwable) {
        throw err
      }
    }
  }
}

actual class BookmarksDatabaseFactory() {
  actual fun createDatabase() : BookmarksDatabase {
    val driver = NativeSqliteDriver(
      schema = BookmarksDatabase.Schema,
      name = "bookmarks_db",
    )
    return BookmarksDatabase(driver)
  }
}

