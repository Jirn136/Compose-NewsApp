package com.zoho.weatherapp.remote

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.zoho.weatherapp.database.NewsDatabase
import com.zoho.weatherapp.database.NewsEntity
import com.zoho.weatherapp.remote.api.NewsApi
import com.zoho.weatherapp.remote.mappers.toNewsEntity
import java.io.IOException

@OptIn(ExperimentalPagingApi::class)
class NewsRemoteMediator(
    private val newsDb: NewsDatabase,
    private val newsApi: NewsApi
) : RemoteMediator<Int, NewsEntity>() {
    private val limit = 15
    private var initialOffset = 0
    private var nextOffset = 0
    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, NewsEntity>
    ): MediatorResult {
        return try {
            val loadKey = when (loadType) {
                LoadType.REFRESH -> initialOffset
                LoadType.PREPEND -> return MediatorResult.Success(endOfPaginationReached = true)
                LoadType.APPEND -> {
                    val lastState = state.pages.lastOrNull()
                    lastState?.let {
                        nextOffset += limit
                    } ?: run {
                        return MediatorResult.Success(endOfPaginationReached = true)
                    }
                    nextOffset
                }
            }

            val news = newsApi.getNews(limit = limit, offset = loadKey)
            newsDb.withTransaction {
                if (loadType == LoadType.REFRESH) newsDb.dao.clearAll()
                val newsEntity = news.toNewsEntity()
                newsDb.dao.insertNews(newsEntity)
            }
            MediatorResult.Success(endOfPaginationReached = news.next.isNullOrEmpty())

        } catch (e: IOException) {
            e.printStackTrace()
            MediatorResult.Error(e)
        } catch (e: Exception) {
            e.printStackTrace()
            MediatorResult.Error(e)
        }
    }
}