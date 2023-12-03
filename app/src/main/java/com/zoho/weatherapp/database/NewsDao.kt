package com.zoho.weatherapp.database

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert

@Dao
interface NewsDao {
    @Upsert
    suspend fun insertNews(news: List<NewsEntity>)

    @Query("SELECT * FROM newsentity ORDER BY id DESC")
    fun pagingSource(): PagingSource<Int, NewsEntity>

    @Query("SELECT * FROM newsentity WHERE title LIKE '%' || :text || '%' OR newsSite LIKE '%' || :text || '%' OR summary LIKE '%' || :text || '%' ORDER BY id DESC")
    fun getAllNews(text: String): PagingSource<Int, NewsEntity>

    @Query("DELETE FROM newsentity")
    suspend fun clearAll()
}