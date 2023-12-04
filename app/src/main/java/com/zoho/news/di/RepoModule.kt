package com.zoho.news.di

import com.zoho.news.screens.news.airQuality.AirQualityRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepoModule {

    @Provides
    @Singleton
    fun provideApiQualityRepository(): AirQualityRepository =
        AirQualityRepository(airQualityApi = AppModule.provideAirApi())
}