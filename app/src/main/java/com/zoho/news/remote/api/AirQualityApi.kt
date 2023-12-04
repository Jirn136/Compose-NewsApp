package com.zoho.news.remote.api

import com.zoho.news.remote.model.AirQualityDto
import com.zoho.news.utils.Constants
import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface AirQualityApi {

    @GET("air_pollution")
    suspend fun getAirQuality(
        @Query(Constants.LATITUDE) latitude: String = "0",
        @Query(Constants.LONGITUDE) longitude: String = "0",
        @Query(Constants.APP_ID) apiId: String = Constants.WEATHER_API_KEY,
    ): Deferred<Response<AirQualityDto>>

    companion object {
        const val BASE_URL = "http://api.openweathermap.org/data/2.5/"
    }
}