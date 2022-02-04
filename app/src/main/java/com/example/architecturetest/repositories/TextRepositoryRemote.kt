package com.example.architecturetest.repositories

import android.content.Context
import android.util.Log
import com.example.architecturetest.data.Thing
import com.example.architecturetest.data.createDatabase
import kotlinx.coroutines.flow.MutableStateFlow
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.GET

/**
 * This doesn't add anything as yet...
 * It's just to test using retofit and okhttp
 */
class TextRepositoryRemote(context: Context): TextRepository {

  override val allText = MutableStateFlow<List<String>>(listOf())

  data class Thing(
    val name: String,
    val number: Int,
  )

  interface RandomTextFile {
    @GET("/m/random.json")
    //suspend fun getSearchPage(@Query("search_query") query: String):String
    suspend fun getFile():List<Thing>
  }

  val service:RandomTextFile = Retrofit.Builder()
    .baseUrl("https://mmmnnnmmm.com")
    .addConverterFactory(GsonConverterFactory.create())
    .build()
    .create(RandomTextFile::class.java)

  override suspend fun addText(text: String) {
    refresh()
  }

  override suspend fun refresh() {
    try {
      val file = service.getFile()
      allText.value = file.map { "${it.number}" }
    } catch(e: Exception) {
      Log.d("HI", "error!")
    }
  }

}
