package com.zoho.news.screens.news.airQuality

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.widget.Toast
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.zoho.news.utils.Constants
import com.zoho.news.utils.isNetworkAvailable
import com.zoho.news.utils.toToast
import com.zoho.weatherapp.R

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun AirQualityScreen(modifier: Modifier = Modifier) {
    val airViewModel = hiltViewModel<AirQualityViewModel>()
    val context = LocalContext.current


    val fusedLocationClient: FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(
            context
        )

    val permissionsList = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        val permissionsList = Constants.LOCATION_PERMISSIONS.toMutableList()
        permissionsList.add(Manifest.permission.ACCESS_BACKGROUND_LOCATION)
        permissionsList.toTypedArray()
    } else Constants.LOCATION_PERMISSIONS.toTypedArray()


    val locationPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val allGranted = permissions.all { it.value }
        if (allGranted) {
            getLastKnownLocation(
                context = context,
                fusedLocationClient = fusedLocationClient,
                airViewModel = airViewModel
            )
        } else Toast.makeText(context, "Permission is required", Toast.LENGTH_SHORT).show()

    }
    val multiplePermissionState =
        rememberMultiplePermissionsState(permissions = permissionsList.toList())

    Row {
        AirQualityData(modifier.weight(0.8f), airViewModel)
        IconButton(
            onClick = {
                if (context.isNetworkAvailable()) {
                    locationPermissionLauncher.launch(permissionsList)
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

private fun getLastKnownLocation(
    context: Context,
    fusedLocationClient: FusedLocationProviderClient,
    airViewModel: AirQualityViewModel
) {
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
    fusedLocationClient.lastLocation.addOnSuccessListener {
        it?.let {
            airViewModel.getAirQuality(it.latitude.toString(), it.longitude.toString())
            context.getString(R.string.fetching_data_please_wait).toToast(context)
        } ?: run {
            context.getString(R.string.unable_to_fetch_data_please_try_again_later).toToast(context)
        }
    }
}