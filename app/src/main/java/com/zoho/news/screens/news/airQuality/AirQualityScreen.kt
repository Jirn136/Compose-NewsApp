package com.zoho.news.screens.news.airQuality

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
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
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.zoho.news.utils.Constants

@Composable
fun AirQualityScreen(modifier: Modifier = Modifier) {
    val airViewModel = hiltViewModel<AirQualityViewModel>()
    val context = LocalContext.current


    val fusedLocationClient: FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(
            context
        )

    val locationPermissionLauncher = rememberLauncherForActivityResult(
        contract =
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val allGranted = permissions.all { it.value }
        if (allGranted) {
            getLastKnownLocation(
                context = context,
                fusedLocationClient = fusedLocationClient,
                airViewModel = airViewModel
            )
        }

    }

    Row {
        AirQualityData(modifier.weight(0.7f), airViewModel)
        IconButton(
            onClick = {
                val permissions = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    val permissionsList = Constants.LOCATION_PERMISSIONS.toMutableList()
                    permissionsList.add(Manifest.permission.ACCESS_BACKGROUND_LOCATION)
                    permissionsList.toTypedArray()
                } else Constants.LOCATION_PERMISSIONS.toTypedArray()
                locationPermissionLauncher.launch(permissions)
            },
            modifier
                .align(Alignment.CenterVertically)
                .padding(top = 5.dp)
                .weight(0.2f)
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
        airViewModel.getAirQuality(it.latitude.toString(), it.longitude.toString())
    }
}