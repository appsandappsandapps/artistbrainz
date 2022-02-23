package appsandapps.artistbrainz.datasources

import appsandapps.artistbrainz.BookmarksDatabase
import appsandapps.artistbrainz.BookmarksQueries
import appsandapps.artistbrainz.data.Bookmark
import appsandapps.artistbrainz.repositories.BookmarksDatasource

class BookmarksDatastoreSqlDelight(
  db: BookmarksDatabase,
  private val dao: BookmarksQueries = db.bookmarksQueries,
): BookmarksDatasource {
  override suspend fun getAll(): List<Bookmark> =
    dao.getAll().executeAsList().map { Bookmark(it.id, it.name) }

  override suspend fun get(id: String): Bookmark? =
    dao.get(id).executeAsOneOrNull()?.let { Bookmark(it.id, it.name) }

  override suspend fun delete(id: String) =
    dao.delete(id)

  override suspend fun insert(id: String, name: String) =
    dao.insert(id, name)
}
