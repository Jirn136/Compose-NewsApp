package com.zoho.news.screens.news.airQuality

import com.zoho.news.domain.AirQuality
import com.zoho.news.remote.api.AirQualityApi
import com.zoho.news.remote.mappers.toAirQuality
import com.zoho.news.remote.state.LoadingState
import javax.inject.Inject

class AirQualityRepository @Inject constructor(
    private val airQualityApi: AirQualityApi
) {
    suspend fun getAirQualityApi(lat: String, long: String): LoadingState<AirQuality> {
        val apiRequest = airQualityApi.getAirQuality(latitude = lat, longitude = long).await()
        return if (apiRequest.isSuccessful) {
            apiRequest.body()?.let {
                LoadingState.Success(it.toAirQuality())
            } ?: run {
                LoadingState.Error(apiRequest.message())
            }
        } else {
            LoadingState.Error(apiRequest.message().toString())
        }
    }
}