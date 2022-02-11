package appsandapps.artistbrainz.data.datasources

import android.content.Context
import androidx.room.*
import appsandapps.artistbrainz.data.Bookmark
import appsandapps.artistbrainz.repositories.BookmarksDatasource

@Entity
private data class BookmarkEntity (
  @PrimaryKey val id: String,
  @ColumnInfo(name = "name") val name: String,
)

@Dao
private interface BookmarkDao {
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
private abstract class BookmarksDatabase : RoomDatabase() {
  abstract fun bookmarksDao() : BookmarkDao
}

class BookmarksDatasourceRoomDb(
  private val context: Context
): BookmarksDatasource {

  private val db: BookmarkDao

  init {
    db = Room
      .databaseBuilder(context, BookmarksDatabase::class.java, "artistbrainz_bms_db")
      .build()
      .bookmarksDao()
  }

  override suspend fun getAll(): List<Bookmark> =
    db.getAll().map { Bookmark(it.id, it.name) }

  override suspend fun get(id: String): Bookmark? =
    db.get(id)?.let { Bookmark(it.id, it.name) }

  override suspend fun delete(id: String) = db.delete(id)

  override suspend fun insert(id: String, name: String) =
    db.insert(BookmarkEntity(id, name))
}

