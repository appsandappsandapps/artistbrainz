package appsandapps.artistbrainz.utils

import appsandapps.artistbrainz.BookmarksDatabase

/**
 * This creates the database with a sqldelight driver.
 *
 * This would be changed in a KMM project
 */
expect class BookmarksDatabaseFactory {
  fun createDatabase() : BookmarksDatabase
}