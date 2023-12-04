package com.zoho.news.utils

import android.Manifest

object Constants {
    const val WEATHER_API_KEY = "1278b1bc49d5d45692a87b8d9807f9a2"
    const val DEFAULT_PAGE_sIZE = 20
    const val LATITUDE = "lat"
    const val LONGITUDE = "lon"
    const val APP_ID = "appid"
    val LOCATION_PERMISSIONS = arrayListOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION
    )
}