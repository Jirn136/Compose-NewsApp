package com.zoho.news.screens.news.airQuality

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Row
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.zoho.news.utils.Prefs
import com.zoho.news.utils.isNetworkAvailable
import com.zoho.news.utils.redirectToSettings
import com.zoho.news.utils.toToast
import com.zoho.weatherapp.R

@Composable
fun AirQualityScreen(modifier: Modifier = Modifier) {
    val airViewModel = hiltViewModel<AirQualityViewModel>()
    val context = LocalContext.current
    val activity = LocalContext.current as Activity

    var permissionDeniedCount by remember {
        mutableIntStateOf(Prefs.getPermissionDeniedCount(context))
    }


    val fusedLocationClient: FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(
            context
        )

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
    ) { isGranted ->
        if (isGranted)
            getLastKnownLocation(context, fusedLocationClient, airViewModel)
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

    Row {
        AirQualityData(modifier.weight(0.8f), airViewModel)
        IconButton(
            onClick = {
                if (context.isNetworkAvailable()) {
                    if (ContextCompat.checkSelfPermission(
                            context,
                            Manifest.permission.ACCESS_FINE_LOCATION
                        ) == PackageManager.PERMISSION_GRANTED
                    ) getLastKnownLocation(context, fusedLocationClient, airViewModel)
                    else
                        permissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
                } else context.getString(R.string.no_internet_available_please_try_again_later)
                    .toToast(context)
            },
            modifier
                .align(Alignment.CenterVertically)
                .padding(top = 10.dp)
                .weight(0.15f)
        ) {
            Icon(
                imageVector = Icons.Rounded.Refresh,
                contentDescription = "Refresh",
                modifier = modifier
                    .size(25.dp)
                    .align(Alignment.CenterVertically)
            )
        }
    }
}

@SuppressLint("MissingPermission")
private fun getLastKnownLocation(
    context: Context,
    fusedLocationClient: FusedLocationProviderClient,
    airViewModel: AirQualityViewModel
) {
    fusedLocationClient.lastLocation.addOnSuccessListener {
        it?.let {
            airViewModel.getAirQuality(it.latitude.toString(), it.longitude.toString())
            context.getString(R.string.fetching_data_please_wait).toToast(context)
        } ?: run {
            context.getString(R.string.unable_to_fetch_data_please_try_again_later).toToast(context)
        }
    }
}