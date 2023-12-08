package com.zoho.news.screens.news.airQuality

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.location.LocationManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Refresh
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.zoho.news.utils.Prefs
import com.zoho.news.utils.isNetworkAvailable
import com.zoho.news.utils.redirectToLocationSettings
import com.zoho.news.utils.redirectToSettings
import com.zoho.news.utils.toToast
import com.zoho.weatherapp.R

@Composable
fun AirQualityScreen(modifier: Modifier = Modifier) {
    val airViewModel = hiltViewModel<AirQualityViewModel>()
    val context = LocalContext.current
    val activity = LocalContext.current as Activity
    val fusedLocationClient: FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(
            context
        )
    val locationCallback: LocationCallback = object : LocationCallback() {
        override fun onLocationResult(p0: LocationResult) {
            super.onLocationResult(p0)
            for (location in p0.locations) {
                airViewModel.getAirQuality(
                    location.latitude.toString(),
                    location.longitude.toString()
                )
            }
        }
    }


    var permissionDeniedCount by remember {
        mutableIntStateOf(Prefs.getPermissionDeniedCount(context))
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
    ) { isGranted ->
        if (isGranted)
            checkLocationServices(context, fusedLocationClient, locationCallback)
        else {
            permissionDeniedCount++
            Prefs.insertPermissionDeniedCount(context, permissionDeniedCount)
            if (permissionDeniedCount > 2) {
                context.getString(R.string.in_order_to_check_air_quality_location_permission_is_must)
                    .toToast(context)
                activity.redirectToSettings()
            } else
                context.getString(R.string.need_location_permission).toToast(context)
        }
    }

    Row(modifier = modifier.fillMaxWidth()) {
        AirQualityData(modifier.weight(0.9f), airViewModel)
        IconButton(
            onClick = {
                if (context.isNetworkAvailable()) {
                    if (ContextCompat.checkSelfPermission(
                            context,
                            Manifest.permission.ACCESS_FINE_LOCATION
                        ) == PackageManager.PERMISSION_GRANTED
                    ) checkLocationServices(context, fusedLocationClient, locationCallback)
                    else
                        permissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
                } else context.getString(R.string.no_internet_available_please_try_again_later)
                    .toToast(context)
            },
            modifier
                .align(Alignment.CenterVertically)
                .padding(top = 10.dp, end = 10.dp)
                .weight(0.1f)
        ) {
            Icon(
                imageVector = Icons.Rounded.Refresh,
                contentDescription = "Refresh",
                tint = if (isSystemInDarkTheme()) Color.White else Color.Black,
                modifier = modifier
                    .size(20.dp)
                    .align(Alignment.CenterVertically)
            )
        }
    }
}

fun checkLocationServices(
    context: Context,
    fusedLocationClient: FusedLocationProviderClient,
    locationCallback: LocationCallback
) {
    val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
    if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER))
        context.redirectToLocationSettings()
    else getLocation(context, fusedLocationClient, locationCallback)
}

private fun getLocation(
    context: Context,
    fusedLocationClient: FusedLocationProviderClient,
    locationCallback: LocationCallback
) {
    context.getString(R.string.fetching_data_please_wait).toToast(context)
    val locationRequest = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 100)
        .setWaitForAccurateLocation(false)
        .setMaxUpdates(1)
        .build()

    if (ActivityCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) != PackageManager.PERMISSION_GRANTED
    ) {
        return
    }
    fusedLocationClient.requestLocationUpdates(
        locationRequest, locationCallback, null
    )
}