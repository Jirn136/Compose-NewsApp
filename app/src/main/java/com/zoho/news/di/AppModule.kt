package com.zoho.news.di

import android.content.Context
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.room.Room
import com.zoho.news.database.NewsDatabase
import com.zoho.news.database.NewsEntity
import com.zoho.news.remote.NewsRemoteMediator
import com.zoho.news.remote.api.AirQualityApi
import com.zoho.news.remote.api.NewsApi
import com.zoho.news.utils.Constants.DEFAULT_PAGE_SIZE
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.create
import javax.inject.Singleton

@OptIn(ExperimentalPagingApi::class)
@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideNewsDatabase(@ApplicationContext context: Context): NewsDatabase {
        return Room.databaseBuilder(
            context, NewsDatabase::class.java, name = "news.db"
        ).build()
    }

    @Provides
    @Singleton
    fun provideNewsApi(): NewsApi {
        return Retrofit.Builder().baseUrl(NewsApi.BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create())
            .build().create()
    }

    @Provides
    @Singleton
    fun provideAirApi(): AirQualityApi {
        return Retrofit.Builder().baseUrl(AirQualityApi.BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create())
            .build().create()
    }

    @Provides
    @Singleton
    fun provideNewsPager(newsDb: NewsDatabase, newsApi: NewsApi): Pager<Int, NewsEntity> {
        return Pager(
            config = PagingConfig(pageSize = DEFAULT_PAGE_SIZE),
            remoteMediator = NewsRemoteMediator(
                newsDb = newsDb, newsApi = newsApi
            ), pagingSourceFactory = {
                newsDb.dao.pagingSource()
            }
        )
    }
}