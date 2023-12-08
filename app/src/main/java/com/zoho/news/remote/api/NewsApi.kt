package com.zoho.news.remote.api

import com.zoho.news.remote.model.NewsDto
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsApi {
    @GET("articles/")
    suspend fun getNews(
        @Query("format") format: String = "json",
        @Query("limit") limit: Int,
        @Query("offset") offset: Int
    ): NewsDto

    companion object {
        const val BASE_URL = "https://api.spaceflightnewsapi.net/v4/"
    }
}