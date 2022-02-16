package appsandapps.artistbrainz.datasources

import android.content.Context
import androidx.room.*
import appsandapps.artistbrainz.data.Bookmark
import appsandapps.artistbrainz.repositories.BookmarksDatasource

@Entity
data class BookmarkEntity (
  @PrimaryKey val id: String,
  @ColumnInfo(name = "name") val name: String,
)

@Dao
interface BookmarkDao {
  @Query("SELECT * FROM bookmarkentity")
  suspend fun getAll(): List<BookmarkEntity>

  @Query("SELECT * FROM bookmarkentity where id = :id LIMIT 1")
  suspend fun get(id: String): BookmarkEntity?

  @Query("DELETE from bookmarkentity where id = :id")
  suspend fun delete(id: String)

  @Insert
  suspend fun insert(task: BookmarkEntity)
}

@Database(entities = arrayOf(BookmarkEntity::class), version = 1)
abstract class BookmarksDatabase : RoomDatabase() {
  abstract fun bookmarksDao() : BookmarkDao
}

fun createBookmarksDatabase(context: Context): BookmarksDatasource {
  val db = Room.databaseBuilder(context,
    BookmarksDatabase::class.java, "artistbrainz_bookmarks_db").build()
  val dao = db.bookmarksDao()
  return object : BookmarksDatasource {
    override suspend fun getAll(): List<Bookmark> =
      dao.getAll().map { Bookmark(it.id, it.name) }

    override suspend fun get(id: String): Bookmark? =
      dao.get(id)?.let { Bookmark(it.id, it.name) }

    override suspend fun delete(id: String) =
      dao.delete(id)

    override suspend fun insert(id: String, name: String) =
      dao.insert(BookmarkEntity(id, name))
  }
}
