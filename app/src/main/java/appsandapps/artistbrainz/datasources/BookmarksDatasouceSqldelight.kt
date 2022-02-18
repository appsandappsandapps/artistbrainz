package appsandapps.artistbrainz.datasources

import appsandapps.artistbrainz.BookmarksDatabase
import appsandapps.artistbrainz.data.Bookmark
import appsandapps.artistbrainz.repositories.BookmarksDatasource

fun createBookmarksDatastore(db: BookmarksDatabase): BookmarksDatasource {
  val dao = db.bookmarksQueries
  return object : BookmarksDatasource {
    override suspend fun getAll(): List<Bookmark> =
      dao.getAll().executeAsList().map { Bookmark(it.id, it.name) }

    override suspend fun get(id: String): Bookmark? =
      dao.get(id).executeAsOneOrNull()?.let { Bookmark(it.id, it.name) }

    override suspend fun delete(id: String) =
      dao.delete(id)

    override suspend fun insert(id: String, name: String) =
      dao.insert(id, name)
  }
}
