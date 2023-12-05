package com.zoho.news.utils

import androidx.compose.ui.graphics.Color
import com.zoho.news.domain.AirQuality

fun AirQuality.toProgressData(): List<Pair<String, Float>> {
    val so2Range = when (so2) {
        in 0.0..20.0 -> 1f
        in 20.0..80.0 -> 2f
        in 80.0..250.0 -> 3f
        in 250.0..350.0 -> 4f
        else -> 5f
    }
    val no2Range = when (no2) {
        in 0.0..40.0 -> 1f
        in 40.0..70.0 -> 2f
        in 70.0..150.0 -> 3f
        in 150.0..200.0 -> 4f
        else -> 5f
    }

    val pm10Range = when (pm10) {
        in 0.0..20.0 -> 1f
        in 20.0..50.0 -> 2f
        in 50.0..100.0 -> 3f
        in 100.0..200.0 -> 4f
        else -> 5f
    }

    val pm25Range = when (pm25) {
        in 0.0..10.0 -> 1f
        in 10.0..25.0 -> 2f
        in 25.0..50.0 -> 3f
        in 50.0..75.0 -> 4f
        else -> 5f
    }

    val o3Range = when (o3) {
        in 0.0..60.0 -> 1f
        in 60.0..100.0 -> 2f
        in 100.0..140.0 -> 3f
        in 140.0..180.0 -> 4f
        else -> 5f
    }

    val coRange = when (co) {
        in 0.0..4000.0 -> 1f
        in 4000.0..9400.0 -> 2f
        in 9400.0..12400.0 -> 3f
        in 12400.0..15400.0 -> 4f
        else -> 5f
    }
    val nh3Range = if (nh3 in 0.1..200.0) 1f else 2f
    val noRange = if (no in 0.1..100.0) 1f else 2f
    return listOf(
        Pair("so2", so2Range),
        Pair("no2", no2Range),
        Pair("pm10", pm10Range),
        Pair("pm25", pm25Range),
        Pair("o3", o3Range),
        Pair("co", coRange),
        Pair("nh3", nh3Range),
        Pair("no", noRange),
    )
}

fun List<Pair<String, Float>>.getReadableText(): Pair<String, Color> {
    return when {
        all { it.second == 1f } -> Pair("Good", Color.Green)
        all { it.second == 2f } || any { it.second == 2f } -> Pair("Fair", Color.Yellow)
        all { it.second == 3f } || any { it.second == 3f } -> Pair("Moderate", Color(0xFFFFA500))
        all { it.second == 4f } || any { it.second == 4f } -> Pair("Poor", Color.Red)
        else -> Pair("Very Poor", Color(0xFF8B0000))
    }
}