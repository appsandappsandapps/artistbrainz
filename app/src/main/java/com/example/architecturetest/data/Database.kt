package com.example.architecturetest.data

import android.content.Context
import androidx.room.*

@Entity
data class Thing (
  @PrimaryKey(autoGenerate = true) val uid: Int = 0,
  @ColumnInfo(name = "thing_name") val thing: String,
)

@Dao
interface ThingDao {
  @Query("SELECT * FROM thing")
  suspend fun getAll(): List<Thing>

  @Delete
  suspend fun delete(task: Thing)

  @Insert
  suspend fun insert(task: Thing)
}

@Database(entities = arrayOf(Thing::class), version = 1)
abstract class AppDatabase : RoomDatabase() {
  abstract fun thingDao() : ThingDao
}

fun createDatabase(context: Context):ThingDao {
  val db = Room.databaseBuilder(context,
    AppDatabase::class.java, "my_thing_db").build()
  return db.thingDao()
}