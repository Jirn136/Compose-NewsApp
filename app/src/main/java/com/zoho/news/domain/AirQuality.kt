package com.zoho.news.domain

data class AirQuality(
    val co: Double = 0.0,
    val nh3: Double = 0.0,
    val no: Double = 0.0,
    val no2: Double = 0.0,
    val o3: Double = 0.0,
    val pm10: Double = 0.0,
    val pm25: Double = 0.0,
    val so2: Double = 0.0
)
