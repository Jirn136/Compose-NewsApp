package com.zoho.news.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.widget.Toast
import androidx.compose.ui.graphics.Color
import com.zoho.news.domain.AirQuality
import com.zoho.weatherapp.R
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

fun AirQuality.toProgressData(context: Context): List<Triple<String, Float, Double>> {
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
        Triple(context.getString(R.string.so2), so2Range, so2),
        Triple(context.getString(R.string.no2), no2Range, no2),
        Triple(context.getString(R.string.pm10), pm10Range, pm10),
        Triple(context.getString(R.string.pm25), pm25Range, pm25),
        Triple(context.getString(R.string.o3), o3Range, o3),
        Triple(context.getString(R.string.co), coRange, co),
        Triple(context.getString(R.string.nh3), nh3Range, nh3),
        Triple(context.getString(R.string.no), noRange, no),
    )
}

fun List<Triple<String, Float, Double>>.getReadableText(context: Context): Pair<String, Color> {
    return when {
        all { it.second == 1f } -> Pair(context.getString(R.string.good), Color.Green)
        all { it.second == 2f } || any { it.second == 2f } -> Pair(
            context.getString(R.string.fair),
            Color.Yellow
        )

        all { it.second == 3f } || any { it.second == 3f } -> Pair(
            context.getString(R.string.moderate),
            Color(0xFFFFA500)
        )

        all { it.second == 4f } || any { it.second == 4f } -> Pair(
            context.getString(R.string.poor),
            Color.Red
        )

        else -> Pair(context.getString(R.string.very_poor), Color(0xFF8B0000))
    }
}

fun String.toToast(context: Context) =
    Toast.makeText(context, this, Toast.LENGTH_SHORT).show()

fun Context.isNetworkAvailable(): Boolean {
    val isAvailable: Boolean
    val connectivityManager =
        this.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val networkCapabilities = connectivityManager.activeNetwork ?: return false
    val activeConnection =
        connectivityManager.getNetworkCapabilities(networkCapabilities) ?: return false
    isAvailable = when {
        activeConnection.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
        activeConnection.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
        activeConnection.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
        else -> false
    }
    return isAvailable
}

fun String.convertReadableTimeStamp(): String {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'")
        val dateTime = LocalDateTime.parse(this, formatter)
        val dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")
        dateTime.format(dateFormatter)
    } else {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault())
        val outputFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
        try {
            val date = inputFormat.parse(this)
            date?.let { outputFormat.format(it) } ?: Constants.EMPTY_STRING
        } catch (e: Exception) {
            Constants.EMPTY_STRING
        }
    }
}

fun Activity.redirectToSettings() {
    Intent(
        Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
        Uri.fromParts("package", packageName, null)
    ).also(::startActivity)
}