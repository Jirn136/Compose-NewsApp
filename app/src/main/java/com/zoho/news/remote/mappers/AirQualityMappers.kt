package com.zoho.news.remote.mappers

import com.zoho.news.domain.AirQuality
import com.zoho.news.remote.model.AirQualityDto


fun AirQualityDto.toAirQuality(): AirQuality {
    return AirQuality(
        co = co,
        nh3 = nh3,
        no = no,
        no2 = no2,
        o3 = o3,
        pm10 = pm10,
        pm25 = pm25,
        so2 = so2
    )
}