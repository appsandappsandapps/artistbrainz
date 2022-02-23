package appsandapps.artistbrainz.utils

import android.content.Context
import appsandapps.artistbrainz.BookmarksDatabase
import com.squareup.sqldelight.android.AndroidSqliteDriver

/**
 * This creates the database with a sqldelight driver.
 *
 * This would be changed in a KMM project
 */
expect class BookmarksDatabaseFactory {
  fun createDatabase() : BookmarksDatabase
}