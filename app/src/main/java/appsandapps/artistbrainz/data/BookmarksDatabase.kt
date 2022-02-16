package appsandapps.artistbrainz.data

import android.content.Context
import androidx.room.*

@Entity
data class BookmarkEntity (
  @PrimaryKey val id: String,
  @ColumnInfo(name = "name") val name: String,
)

@Dao
interface BookmarkDao {
  @Query("SELECT * FROM bookmarkentity order")
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

fun createBookmarksDatabase(context: Context):BookmarkDao {
  val db = Room.databaseBuilder(context,
    BookmarksDatabase::class.java, "swapcard_db").build()
  return db.bookmarksDao()
}
