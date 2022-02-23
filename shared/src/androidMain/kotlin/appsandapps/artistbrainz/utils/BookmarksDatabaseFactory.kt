package appsandapps.artistbrainz.utils

import android.content.Context
import appsandapps.artistbrainz.BookmarksDatabase
import com.squareup.sqldelight.android.AndroidSqliteDriver

/**
 * This creates the database with a sqldelight driver.
 *
 * This would be changed in a KMM project
 */
actual class BookmarksDatabaseFactory(val context: Context) {
  actual fun createDatabase() : BookmarksDatabase {
    val driver = AndroidSqliteDriver(
      schema = BookmarksDatabase.Schema,
      context = context,
      name = "bookmarks_db",
    )
    return BookmarksDatabase(driver)
  }
}
