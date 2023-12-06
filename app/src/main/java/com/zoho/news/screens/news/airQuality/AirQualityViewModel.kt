package com.zoho.news.screens.news.airQuality

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zoho.news.domain.AirQuality
import com.zoho.news.remote.state.LoadingState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AirQualityViewModel @Inject constructor(
    private val airQualityRepository: AirQualityRepository
) : ViewModel() {

    private val _airQuality = MutableLiveData<LoadingState<AirQuality>>()
    val airQuality: LiveData<LoadingState<AirQuality>> get() = _airQuality

    fun getAirQuality(lat: String, long: String) {
        viewModelScope.launch {
            _airQuality.apply {
                value = LoadingState.Loading
                value = airQualityRepository.getAirQualityApi(lat, long)
            }
        }
    }
}